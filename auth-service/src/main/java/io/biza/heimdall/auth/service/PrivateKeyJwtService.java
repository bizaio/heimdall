package io.biza.heimdall.auth.service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Validator;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenPrivateKeyJwt;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.shared.enumerations.HeimdallTokenType;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.TokenData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.util.JoseSigningUtil;
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

  public ResponseEntity<TokenResponse> tokenLogin(RequestTokenPrivateKeyJwt request)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      NotInitialisedException, CryptoException {
    /**
     * The Validator must pass
     */
    if (validator.validate(request).size() > 0) {
      throw new InvalidRequestException();
    }

    /**
     * All client identifiers in Heimdall are UUID so we attempt parsing immediately.
     */
    UUID clientId;
    try {
      clientId = UUID.fromString(request.clientId());
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
    if (holderClient.clientSecret() != null && !holderClient.clientSecret().equals(request.clientSecret())) {
      LOG.error("Client Secret for ({}) is incorrect", clientId);
      throw new InvalidClientException();
    }

    /**
     * Requested scopes exceed the available scopes
     */
    if (request.scopes() != null && !List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ).containsAll(request.scopes())) {
      throw new InvalidScopeException();
    }

    /**
     * Process the supplied assertion
     */
    try {
      JoseSigningUtil.verify(request.clientAssertion(), holderClient.softwareProduct().jwksUri(),
          holderClient.id().toString(), EndpointUtil.tokenEndpoint().toString());
    } catch (JoseException e) {
      LOG.error("Encountered Generic Jose4j Exception", e);
      throw CryptoException.builder().jose(e).build();
    } catch (InvalidJwtException e) {
      LOG.warn("Supplied JWT did not pass signing", e);
      throw CryptoException.builder().invalidJwt(e).build();
    } catch (IOException | InterruptedException e) {
      LOG.error("Encountered generic signing verification error", e);
      throw CryptoException.builder().build();
    }

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
