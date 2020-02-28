package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;

public interface BankingDataRecipientApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
 
  default ResponseEntity<DioDataRecipient> createRecipient(DioDataRecipient recipient){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataRecipient>> listRecipients(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipient> getRecipient(UUID recipientId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipient> updateRecipient(UUID recipientId,
      @NotNull DioDataRecipient recipient){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteRecipient(UUID recipientId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
