package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderBrandApiDelegate;
import io.biza.heimdall.admin.model.payloads.DioDataHolderBrand;
import io.biza.heimdall.payload.responses.RequestGetDataHolderBrands;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;

@Validated
@Controller
public class BankingDataHolderBrandApiDelegateImpl implements BankingDataHolderBrandApiDelegate {

  @Override
  public ResponseEntity<DioDataHolderBrand> createHolderBrand(DioDataHolderBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<List<DioDataHolderBrand>> listHolderBrands(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataHolderBrand> getHolderBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataHolderBrand> updateHolderBrand(UUID holderId,
      @NotNull DioDataHolderBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<Void> deleteHolderBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
