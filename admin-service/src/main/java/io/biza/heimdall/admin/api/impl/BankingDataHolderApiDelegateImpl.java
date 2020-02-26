package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.admin.model.payloads.DioDataHolder;
import io.biza.heimdall.payload.responses.RequestGetDataHolderBrands;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;

@Validated
@Controller
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {

  @Override
  public ResponseEntity<DioDataHolder> createHolder(DioDataHolder holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<List<DioDataHolder>> listHolders(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataHolder> getHolder(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataHolder> updateHolder(UUID holderId,
      @NotNull DioDataHolder holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<Void> deleteHolder(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
