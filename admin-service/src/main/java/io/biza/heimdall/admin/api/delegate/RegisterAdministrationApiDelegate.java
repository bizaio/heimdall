package io.biza.heimdall.admin.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.biza.heimdall.shared.payloads.requests.dio.RequestJwkCreate;

public interface RegisterAdministrationApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<String> signCertificate(RequestCACertificateSign createRequest) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
