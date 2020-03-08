package io.biza.heimdall.auth.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;

public interface DiscoveryApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<RawJson> getJwks() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  
  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
