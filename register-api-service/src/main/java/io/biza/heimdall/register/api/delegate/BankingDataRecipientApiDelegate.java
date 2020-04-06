package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.NotFoundException;

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

  default ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId, UUID productId) throws SigningOperationException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<SoftwareProductsStatusList> getSoftwareProductStatuses() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
