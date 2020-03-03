package io.biza.heimdall.register.api.delegate;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.cdr.models.responses.register.RequestGetDataHolderBrands;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandList;

public interface BankingDataHolderApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
      RequestGetDataHolderBrands build) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
