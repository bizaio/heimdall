package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.common.exceptions.NotFoundException;
import io.biza.babelfish.common.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;

public interface BankingDataRecipientBrandApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<DioDataRecipientBrand> createRecipientBrand(UUID recipientId,
      DioDataRecipientBrand brand) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrands(UUID recipientId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipientBrand> getRecipientBrand(UUID recipientId, UUID brandId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(UUID recipientId, UUID brandId,
      DioDataRecipientBrand recipient) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteRecipientBrand(UUID recipientId, UUID brandId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
