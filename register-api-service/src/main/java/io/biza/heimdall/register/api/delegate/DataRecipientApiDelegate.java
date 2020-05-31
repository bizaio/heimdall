package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.common.exceptions.NotFoundException;
import io.biza.babelfish.common.exceptions.NotInitialisedException;
import io.biza.babelfish.common.exceptions.SigningOperationException;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusListV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientListV1;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusListV1;
import io.biza.babelfish.cdr.support.RawJson;

public interface DataRecipientApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseRegisterDataRecipientListV1> getBankingDataRecipients(IndustryType industry) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DataRecipientsStatusListV1> getBankingDataRecipientStatuses(IndustryType industry) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<RawJson> getSoftwareStatementAssertion(IndustryType industry, UUID brandId, UUID productId) throws SigningOperationException, NotFoundException, NotInitialisedException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<SoftwareProductsStatusListV1> getSoftwareProductStatuses(IndustryType industry) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
