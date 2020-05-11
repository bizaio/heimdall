package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.common.exceptions.NotInitialisedException;
import io.biza.babelfish.oidc.payloads.JWKS;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;

public interface DiscoveryApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<JWKS> getJwks() throws NotInitialisedException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
