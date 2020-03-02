package io.biza.heimdall.register.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.exceptions.CryptoException;
import io.biza.heimdall.register.exceptions.InvalidClientException;
import io.biza.heimdall.register.exceptions.InvalidRequestException;
import io.biza.heimdall.register.exceptions.InvalidScopeException;
import io.biza.heimdall.register.exceptions.NotInitialisedException;
import io.biza.heimdall.shared.enumerations.HeimdallTokenType;
import io.biza.heimdall.shared.persistence.model.DataHolderClientData;
import io.biza.heimdall.shared.persistence.model.TokenData;
import io.biza.heimdall.shared.persistence.repository.DataHolderClientRepository;
import io.biza.thumb.oidc.payloads.TokenResponse;
import io.biza.thumb.oidc.requests.RequestTokenClientCredentials;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientCredentialService {

  @Autowired
  Validator validator;

  @Value("${heimdall.token_length_hours}")
  private Integer tokenLength;

  @Autowired
  DataHolderClientRepository clientRepository;

  @Autowired
  TokenIssuanceService issuanceToken;

  public ResponseEntity<TokenResponse> tokenLogin(RequestTokenClientCredentials request)
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

    Optional<DataHolderClientData> optionalHolderClient = clientRepository.findById(clientId);

    /**
     * Holder doesn't exist so client can't be valid
     */
    if (!optionalHolderClient.isPresent()) {
      throw new InvalidClientException();
    }
    DataHolderClientData holderClient = optionalHolderClient.get();

    /**
     * Requested scopes exceed the available scopes
     */
    if (!List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ).containsAll(request.scopes())) {
      throw new InvalidScopeException();
    }

    /**
     * Client Secret doesn't pass
     */
    if (!holderClient.clientSecret().equals(request.clientSecret())) {
      throw new InvalidClientException();
    }

    /**
     * Setup Token Data record
     */
    TokenData token = TokenData.builder().audience(holderClient.id().toString())
        .authenticationTime(OffsetDateTime.now()).dataHolderClient(holderClient)
        .expiry(OffsetDateTime.now().plusHours(tokenLength.longValue())).scopes(request.scopes())
        .tokenType(HeimdallTokenType.ACCESS_TOKEN).build();

    return issuanceToken.createAccessToken(token);
  }


}
