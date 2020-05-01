package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.cdr.exceptions.NotFoundException;
import io.biza.babelfish.cdr.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;

public interface BankingDataRecipientBrandSoftwareProductApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<DioSoftwareProduct> createRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, DioSoftwareProduct softwareProduct) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioSoftwareProduct>> listRecipientBrandSoftwareProducts(
      UUID recipientId, UUID brandId) {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioSoftwareProduct> getRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioSoftwareProduct> updateRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId, @NotNull DioSoftwareProduct recipient) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteRecipientBrandSoftwareProduct(UUID recipientId, UUID brandId,
      UUID softwareProductId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
