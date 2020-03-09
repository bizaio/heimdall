package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandSoftwareProductApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientBrandSoftwareProductApiDelegateImpl
    implements BankingDataRecipientBrandSoftwareProductApiDelegate {

  @Autowired
  SoftwareProductRepository softwareProductRepository;

  @Autowired
  DataRecipientBrandRepository brandRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioSoftwareProduct> createRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, DioSoftwareProduct softwareProduct) throws ValidationListException {

    Optional<DataRecipientBrandData> brand =
        brandRepository.findByIdAndDataRecipientId(brandId, recipientId);

    if (!brand.isPresent()) {
      LOG.warn(
          "Attempted to create a software product for a recipient id of {} and brand id of {} that doesn't exist",
          recipientId, brandId);
      throw ValidationListException.builder().type(HeimdallExceptionType.INVALID_BRAND)
          .explanation(Constants.ERROR_INVALID_BRAND).build();
    }
    SoftwareProductData softwareProductData =
        mapper.map(softwareProduct, SoftwareProductData.class);
    softwareProductData.id(UUID.randomUUID());
    softwareProductData.dataRecipientBrand(brand.get());
    SoftwareProductData savedBrandSoftwareProduct =
        softwareProductRepository.save(softwareProductData);
    LOG.debug("Created a new software product with content of: {}", savedBrandSoftwareProduct);
    return ResponseEntity.ok(mapper.map(savedBrandSoftwareProduct, DioSoftwareProduct.class));
  }

  @Override
  public ResponseEntity<List<DioSoftwareProduct>> listRecipientBrandSoftwareProducts(
      UUID recipientId, UUID brandId) {

    Optional<DataRecipientBrandData> recipient =
        brandRepository.findByIdAndDataRecipientId(brandId, recipientId);

    if (!recipient.isPresent()) {
      LOG.warn("Attempted to list software products on non existent recipient {} and brand of {}",
          recipientId, brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    LOG.debug("Listing all software products and received {}", recipient.get().softwareProducts());
    return ResponseEntity
        .ok(mapper.mapAsList(Optional.of(recipient.get().softwareProducts()).orElse(Set.of()),
            DioSoftwareProduct.class));
  }

  @Override
  public ResponseEntity<DioSoftwareProduct> getRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId) {
    Optional<SoftwareProductData> data = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(softwareProductId,
            brandId, recipientId);

    if (data.isPresent()) {
      LOG.info(
          "Retrieving a single data recipient softwareProduct with recipient of {} and softwareProduct of {} and got content of {}",
          recipientId, softwareProductId, data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioSoftwareProduct.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single recipient {} with softwareProduct of {} and couldn't find it",
          recipientId, softwareProductId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioSoftwareProduct> updateRecipientBrandSoftwareProduct(UUID recipientId,
      UUID brandId, UUID softwareProductId, DioSoftwareProduct updateData) {
    Optional<SoftwareProductData> optionalData = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(softwareProductId,
            brandId, recipientId);

    if (optionalData.isPresent()) {
      SoftwareProductData data = optionalData.get();
      mapper.map(updateData, data);
      data.dataRecipientBrand(optionalData.get().dataRecipientBrand());
      SoftwareProductData updatedData = softwareProductRepository.save(data);

      LOG.info(
          "Updating a single data recipient software product of recipient {}, brand {} and software product {} and now set to {}",
          recipientId, brandId, softwareProductId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioSoftwareProduct.class));
    } else {
      LOG.warn(
          "Attempted to retrieve a single data recipient softwareProduct and could not find with recipient of {} and softwareProduct of {}",
          recipientId, softwareProductId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteRecipientBrandSoftwareProduct(UUID recipientId, UUID brandId,
      UUID softwareProductId) {
    Optional<SoftwareProductData> data = softwareProductRepository
        .findByIdAndDataRecipientBrandIdAndDataRecipientBrandDataRecipientId(softwareProductId,
            brandId, recipientId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data recipient software product with id of {}", recipientId);
      softwareProductRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn(
          "Attempted to retrieve a single data recipient software product and could not find with recipient of {}, brand of {} and software product of {}",
          recipientId, brandId, softwareProductId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
