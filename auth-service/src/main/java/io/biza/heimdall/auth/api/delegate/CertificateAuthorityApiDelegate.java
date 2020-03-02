package io.biza.heimdall.auth.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

public interface CertificateAuthorityApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<String> getCertificateAuthority() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
