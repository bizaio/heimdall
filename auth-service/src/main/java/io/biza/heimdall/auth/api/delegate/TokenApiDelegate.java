package io.biza.heimdall.auth.api.delegate;

import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;
import io.biza.thumb.oidc.payloads.TokenResponse;

public interface TokenApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<TokenResponse> tokenLogin(@NotNull ObjectNode tokenLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  default ResponseEntity<String> getJwks() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
