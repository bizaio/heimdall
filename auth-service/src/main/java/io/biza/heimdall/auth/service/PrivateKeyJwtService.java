package io.biza.heimdall.auth.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.babelfish.oidc.exceptions.InvalidClientException;
import io.biza.babelfish.oidc.exceptions.InvalidRequestException;
import io.biza.babelfish.oidc.exceptions.InvalidScopeException;
import io.biza.babelfish.oidc.payloads.JWTClaims;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.babelfish.oidc.requests.RequestTokenPrivateKeyJwt;
import io.biza.babelfish.spring.exceptions.KeyRetrievalException;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.SigningVerificationException;
import io.biza.babelfish.spring.interfaces.JWKService;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.shared.enumerations.HeimdallTokenType;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.TokenData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivateKeyJwtService {

  @Autowired
  Validator validator;

  @Autowired
  ClientRepository clientRepository;

  @Value("${heimdall.token_length_hours}")
  private Integer tokenLength;

  @Autowired
  TokenIssuanceService issuanceToken;

  @Autowired
  JWKService signingService;

  public ResponseEntity<TokenResponse> tokenLogin(RequestTokenPrivateKeyJwt request)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      SigningOperationException, SigningVerificationException, KeyRetrievalException {

    /**
     * The Validator must pass
     */
    Set<ConstraintViolation<RequestTokenPrivateKeyJwt>> validationResult =
        validator.validate(request);
    if (validationResult.size() > 0) {
      LOG.warn("Input request did not pass initial validation");
      for (ConstraintViolation<RequestTokenPrivateKeyJwt> one : validationResult) {
        LOG.debug("Validation failed for {}: {}", one.getPropertyPath(), one.getMessage());
      }
      throw new InvalidRequestException();
    }

    /**
     * All client identifiers in Heimdall are UUID so we attempt parsing immediately by peeking into
     * the signed payload
     */
    UUID clientId;
    try {
      clientId = UUID.fromString(signingService.peekAtIssuer(request.clientAssertion()));
    } catch (IllegalArgumentException e) {
      throw new InvalidRequestException();
    }

    Optional<ClientData> optionalHolderClient = clientRepository.findById(clientId);


    /**
     * Holder doesn't exist so client can't be valid
     */
    if (!optionalHolderClient.isPresent()) {
      LOG.warn("Client Identifier ({}) cannot be found and is therefore invalid", clientId);
      throw new InvalidClientException();
    }
    ClientData holderClient = optionalHolderClient.get();

    /**
     * Client Secret doesn't pass
     */
    if (holderClient.clientSecret() != null
        && !holderClient.clientSecret().equals(request.clientSecret())) {
      LOG.error("Client Secret for ({}) is incorrect", clientId);
      throw new InvalidClientException();
    }

    /**
     * Requested scopes exceed the available scopes
     */
    if (request.scopes() != null
        && !List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ).containsAll(request.scopes())) {
      throw new InvalidScopeException();
    }

    /**
     * Process the supplied assertion
     */
    signingService.verify(request.clientAssertion(), holderClient.softwareProduct().jwksUri(),
        JWTClaims.builder().issuerByUUID(holderClient.id()).audience(EndpointUtil.tokenEndpoint())
            .build());

    /**
     * Setup Token Data record
     */
    TokenData token = TokenData.builder().audience(holderClient.id().toString())
        .authenticationTime(OffsetDateTime.now()).client(holderClient)
        .expiry(OffsetDateTime.now().plusHours(tokenLength.longValue()))
        .scopes(request.scopes() == null ? List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ)
            : request.scopes())
        .tokenType(HeimdallTokenType.ACCESS_TOKEN).build();

    return issuanceToken.createAccessToken(token);
  }


}
