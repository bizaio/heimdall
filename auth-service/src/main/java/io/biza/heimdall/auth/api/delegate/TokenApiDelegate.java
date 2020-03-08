package io.biza.heimdall.auth.api.delegate;

import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.oidc.exceptions.InvalidClientException;
import io.biza.babelfish.oidc.exceptions.InvalidRequestException;
import io.biza.babelfish.oidc.exceptions.InvalidScopeException;
import io.biza.babelfish.oidc.exceptions.UnsupportedGrantTypeException;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.babelfish.spring.exceptions.KeyRetrievalException;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.SigningVerificationException;

public interface TokenApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<TokenResponse> tokenLogin(Map<String, String> rawLoginRequest) throws InvalidRequestException, InvalidClientException, InvalidScopeException, SigningOperationException, SigningVerificationException, KeyRetrievalException, UnsupportedGrantTypeException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
}
