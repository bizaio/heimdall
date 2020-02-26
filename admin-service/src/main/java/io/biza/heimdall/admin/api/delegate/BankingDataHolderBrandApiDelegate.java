package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.admin.model.payloads.DioDataHolderBrand;
import io.biza.heimdall.payload.responses.RequestGetDataHolderBrands;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;

public interface BankingDataHolderBrandApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
 
  default ResponseEntity<DioDataHolderBrand> createHolderBrand(DioDataHolderBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataHolderBrand>> listHolderBrands(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolderBrand> getHolderBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolderBrand> updateHolderBrand(UUID holderId,
      @NotNull DioDataHolderBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteHolderBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
