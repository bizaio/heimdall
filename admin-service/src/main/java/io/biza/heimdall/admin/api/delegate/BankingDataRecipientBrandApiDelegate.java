package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.heimdall.admin.model.payloads.DioDataRecipientBrand;

public interface BankingDataRecipientBrandApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }
 
  default ResponseEntity<DioDataRecipientBrand> createRecipientBrand(DioDataRecipientBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrands(){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipientBrand> getRecipientBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(UUID holderId,
      @NotNull DioDataRecipientBrand holder){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteRecipientBrand(UUID holderId){
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
