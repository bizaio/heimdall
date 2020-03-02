package io.biza.heimdall.auth.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;

public interface DiscoveryApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<String> getJwks() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
