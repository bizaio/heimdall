package io.biza.heimdall.register.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.payload.responses.ResponseRegisterDataRecipientList;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;

@Validated
@Controller
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {
  @Override
  public ResponseEntity<ResponseRegisterDataRecipientList> getBankingDataRecipients() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
