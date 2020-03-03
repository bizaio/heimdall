package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.heimdall.shared.util.RawJson;

public interface BankingDataRecipientApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseRegisterDataRecipientList> getBankingDataRecipients() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DataRecipientsStatusList> getBankingDataRecipientStatuses() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId, UUID productId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<SoftwareProductsStatusList> getSoftwareProductStatuses() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
