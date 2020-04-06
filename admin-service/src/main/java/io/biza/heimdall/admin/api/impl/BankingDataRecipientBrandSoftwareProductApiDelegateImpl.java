package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandSoftwareProductApiDelegate;
import io.biza.heimdall.shared.component.service.SoftwareProductService;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;
import io.biza.heimdall.shared.persistence.specifications.SoftwareProductSpecifications;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientBrandSoftwareProductApiDelegateImpl
    implements BankingDataRecipientBrandSoftwareProductApiDelegate {

  @Autowired
  SoftwareProductService softwareService;

  @Override
  public ResponseEntity<DioSoftwareProduct> createRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, DioSoftwareProduct softwareProduct)
      throws ValidationListException, NotFoundException {

    return ResponseEntity.ok(softwareService.create(recipientId, brandId, softwareProduct));
  }

  @Override
  public ResponseEntity<List<DioSoftwareProduct>> listRecipientBrandSoftwareProducts(
      UUID recipientId, UUID brandId) {

    return ResponseEntity.ok(softwareService
        .list(SoftwareProductSpecifications.recipientId(recipientId), null)
        .toList());
  }

  @Override
  public ResponseEntity<DioSoftwareProduct> getRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId) throws NotFoundException {
    return ResponseEntity.ok(softwareService.read(recipientId, brandId, softwareProductId));
  }

  @Override
  public ResponseEntity<DioSoftwareProduct> updateRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId, DioSoftwareProduct updateData)
      throws ValidationListException, NotFoundException {

    return ResponseEntity
        .ok(softwareService.update(recipientId, brandId, softwareProductId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteRecipientBrandSoftwareProduct(UUID recipientId, UUID brandId,
      UUID softwareProductId) throws NotFoundException {

    softwareService.delete(recipientId, brandId, softwareProductId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
