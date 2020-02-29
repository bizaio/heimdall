package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.payload.registration.SoftwareStatementAssertion;
import io.biza.heimdall.payload.responses.DataRecipientsStatusList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataRecipientList;
import io.biza.heimdall.payload.responses.SoftwareProductsStatusList;
import io.biza.heimdall.shared.util.RawJson;

public interface RegisterApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
  
  default ResponseEntity<String> getJwks() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<String> getCertificateAuthority() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
