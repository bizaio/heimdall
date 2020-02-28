package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.heimdall.admin.exceptions.ValidationListException;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientBrandApiDelegateImpl implements BankingDataRecipientBrandApiDelegate {

  @Autowired
  DataRecipientBrandRepository brandRepository;

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioDataRecipientBrand> createRecipientBrand(UUID recipientId,
      DioDataRecipientBrand brand) throws ValidationListException {

    Optional<DataRecipientData> recipient = recipientRepository.findById(recipientId);

    if (!recipient.isPresent()) {
      LOG.warn("Attempted to create a recipient brand for a recipient id of {} that doesn't exist",
          recipientId);
      throw ValidationListException.builder().type(HeimdallExceptionType.INVALID_RECIPIENT)
          .explanation(Constants.ERROR_INVALID_RECIPIENT).build();
    }
    DataRecipientBrandData dataRecipientData = mapper.map(brand, DataRecipientBrandData.class);
    dataRecipientData.dataRecipient(recipient.get());
    DataRecipientBrandData savedBrand = brandRepository.save(dataRecipientData);
    LOG.debug("Created a new data recipient with content of: {}", savedBrand);
    return ResponseEntity.ok(mapper.map(savedBrand, DioDataRecipientBrand.class));
  }

  @Override
  public ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrands(UUID recipientId) {

    Optional<DataRecipientData> recipient = recipientRepository.findById(recipientId);

    if (!recipient.isPresent()) {
      LOG.warn("Attempted to list brands on non existent recipient {}", recipientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    LOG.debug("Listing all data recipients and received {}", recipient.get().dataRecipientBrands());
    return ResponseEntity.ok(mapper.mapAsList(
        Optional.of(recipient.get().dataRecipientBrands()).orElse(Set.of()), DioDataRecipientBrand.class));
  }

  @Override
  public ResponseEntity<DioDataRecipientBrand> getRecipientBrand(UUID recipientId, UUID brandId) {
    Optional<DataRecipientBrandData> data = brandRepository.findByIdAndDataRecipientId(brandId, recipientId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single data recipient brand with recipient of {} and brand of {} and got content of {}", recipientId, brandId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioDataRecipientBrand.class));
    } else {
      LOG.warn("Attempted to retrieve a single recipient {} with brand of {} and couldn't find it", 
          recipientId, brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }  }

  @Override
  public ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(UUID recipientId, UUID brandId,
      DioDataRecipientBrand updateData) {
    Optional<DataRecipientBrandData> optionalData = brandRepository.findByIdAndDataRecipientId(brandId, recipientId);

    if (optionalData.isPresent()) {
      DataRecipientBrandData data = optionalData.get();
      mapper.map(updateData, data);
      data.dataRecipient(optionalData.get().dataRecipient());
      DataRecipientBrandData updatedData = brandRepository.save(data);

      LOG.info("Updating a single data recipient brand of recipient {} and brand {} and now set to {}", recipientId, brandId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioDataRecipientBrand.class));
    } else {
      LOG.warn("Attempted to retrieve a single data recipient brand and could not find with recipient of {} and brand of {}",
          recipientId, brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteRecipientBrand(UUID recipientId, UUID brandId) {
    Optional<DataRecipientBrandData> data = brandRepository.findByIdAndDataRecipientId(brandId, recipientId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data recipient brand with id of {}", recipientId);
      brandRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to retrieve a single data recipient brand and could not find with recipient of {} and brand of {}",
          recipientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
