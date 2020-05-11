package io.biza.heimdall.register.api.delegate;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.cdr.models.requests.register.RequestGetDataHolderBrandsV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandListV1;

public interface DataHolderApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<ResponseRegisterDataHolderBrandListV1> getBankingDataHolderBrands(
      RequestGetDataHolderBrandsV1 request, PageRequest pageRequest) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
