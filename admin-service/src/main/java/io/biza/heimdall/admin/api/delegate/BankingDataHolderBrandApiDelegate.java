package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;

public interface BankingDataHolderBrandApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<DioDataHolderBrand> createHolderBrand(UUID holderId,
      DioDataHolderBrand brand) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataHolderBrand>> listHolderBrands(UUID holderId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolderBrand> getHolderBrand(UUID holderId, UUID brandId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolderBrand> updateHolderBrand(UUID holderId, UUID brandId,
      @NotNull DioDataHolderBrand holder) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteHolderBrand(UUID holderId, UUID brandId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
