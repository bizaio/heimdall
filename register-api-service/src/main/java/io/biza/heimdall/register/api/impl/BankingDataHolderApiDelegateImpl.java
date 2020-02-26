package io.biza.heimdall.register.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.payload.responses.RequestGetDataHolderBrands;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;
import io.biza.heimdall.register.api.delegate.BankingDataHolderApiDelegate;

@Validated
@Controller
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {
  @Override
  public ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
      RequestGetDataHolderBrands build) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
