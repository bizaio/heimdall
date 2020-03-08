package io.biza.heimdall.auth.api.delegate;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;

public interface TokenApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<TokenResponse> tokenLogin(Map<String, String> rawLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
}
