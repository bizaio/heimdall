package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.biza.heimdall.shared.payloads.dio.DioRegisterCertificate;
import io.biza.heimdall.shared.payloads.dio.DioRegisterJWK;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.biza.heimdall.shared.payloads.requests.dio.RequestJwkCreate;

public interface RegisterAdministrationApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
 
  default ResponseEntity<DioRegisterJWK> createJwk(RequestJwkCreate createRequest) throws JoseException{
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<String> signCertificate(RequestCACertificateSign createRequest) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
