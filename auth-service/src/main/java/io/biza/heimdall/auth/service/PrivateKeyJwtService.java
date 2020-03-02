package io.biza.heimdall.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.shared.persistence.model.DataHolderClientData;
import io.biza.heimdall.shared.persistence.repository.DataHolderClientRepository;
import io.biza.thumb.oidc.payloads.TokenResponse;
import io.biza.thumb.oidc.requests.RequestTokenPrivateKeyJwt;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PrivateKeyJwtService {

  @Autowired
  Validator validator;

  @Autowired
  DataHolderClientRepository clientRepository;

  public ResponseEntity<TokenResponse> tokenLogin(RequestTokenPrivateKeyJwt request)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException {
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

    Optional<DataHolderClientData> holder = clientRepository.findById(clientId);

    /**
     * Holder doesn't exist so client can't be valid
     */
    if (!holder.isPresent()) {
      throw new InvalidClientException();
    }

    /**
     * Requested scopes exceed the available scopes
     */
    if (!List.of(Constants.SECURITY_SCOPE_REGISTER_BANK_READ).containsAll(request.scopes())) {
      throw new InvalidScopeException();
    }

    /**
     * Client Secret doesn't pass
     */
    /**
     * if (!holder.get().clientSecret().equals(request.clientSecret())) { throw new
     * InvalidClientException(); }
     */


    return null;
  }


}
