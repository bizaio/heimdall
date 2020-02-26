package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.heimdall.admin.model.payloads.DioDataRecipientBrand;

@Validated
@Controller
public class BankingDataRecipientBrandApiDelegateImpl implements BankingDataRecipientBrandApiDelegate {

  @Override
  public ResponseEntity<DioDataRecipientBrand> createRecipientBrand(DioDataRecipientBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrands(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataRecipientBrand> getRecipientBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(UUID holderId,
      @NotNull DioDataRecipientBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
  @Override
  public ResponseEntity<Void> deleteRecipientBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
