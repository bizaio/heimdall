package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.heimdall.shared.component.service.RecipientBrandService;
import io.biza.heimdall.shared.exceptions.NotFoundException;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
import io.biza.heimdall.shared.persistence.specifications.RecipientBrandSpecifications;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientBrandApiDelegateImpl
    implements BankingDataRecipientBrandApiDelegate {

  @Autowired
  RecipientBrandService brandService;

  @Override
  public ResponseEntity<DioDataRecipientBrand> createRecipientBrand(UUID recipientId,
      DioDataRecipientBrand brand) throws ValidationListException, NotFoundException {

    return ResponseEntity.ok(brandService.create(recipientId, brand));
  }

  @Override
  public ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrands(UUID recipientId) {
    return ResponseEntity.ok(brandService
        .list(RecipientBrandSpecifications.recipientId(recipientId), null).toList());
  }

  @Override
  public ResponseEntity<DioDataRecipientBrand> getRecipientBrand(UUID recipientId, UUID brandId)
      throws NotFoundException {
    return ResponseEntity.ok(brandService.read(recipientId, brandId));
  }

  @Override
  public ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(UUID recipientId, UUID brandId,
      DioDataRecipientBrand updateData) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(brandService.update(recipientId, brandId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteRecipientBrand(UUID recipientId, UUID brandId)
      throws NotFoundException {
    brandService.delete(recipientId, brandId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
