package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.admin.model.payloads.DioDataRecipient;

@Validated
@Controller
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

  @Override
  public ResponseEntity<DioDataRecipient> createRecipient(DioDataRecipient holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<List<DioDataRecipient>> listRecipients(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataRecipient> getRecipient(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataRecipient> updateRecipient(UUID holderId,
      @NotNull DioDataRecipient holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<Void> deleteRecipient(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
