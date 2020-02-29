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
import io.biza.heimdall.admin.api.delegate.BankingDataHolderBrandApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderBrandApiDelegateImpl implements BankingDataHolderBrandApiDelegate {

  @Autowired
  DataHolderBrandRepository brandRepository;

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioDataHolderBrand> createHolderBrand(UUID holderId,
      DioDataHolderBrand brand) throws ValidationListException {

    Optional<DataHolderData> holder = holderRepository.findById(holderId);

    if (!holder.isPresent()) {
      LOG.warn("Attempted to create a holder brand for a holder id of {} that doesn't exist",
          holderId);
      throw ValidationListException.builder().type(HeimdallExceptionType.INVALID_HOLDER)
          .explanation(Constants.ERROR_INVALID_HOLDER).build();
    }
    DataHolderBrandData dataHolderData = mapper.map(brand, DataHolderBrandData.class);
    dataHolderData.dataHolder(holder.get());
    DataHolderBrandData savedBrand = brandRepository.save(dataHolderData);
    LOG.debug("Created a new data holder with content of: {}", savedBrand);
    return ResponseEntity.ok(mapper.map(savedBrand, DioDataHolderBrand.class));
  }

  @Override
  public ResponseEntity<List<DioDataHolderBrand>> listHolderBrands(UUID holderId) {

    Optional<DataHolderData> holder = holderRepository.findById(holderId);

    if (!holder.isPresent()) {
      LOG.warn("Attempted to list brands on non existent holder {}", holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    LOG.debug("Listing all data holders and received {}", holder.get().dataHolderBrands());
    return ResponseEntity.ok(mapper.mapAsList(
        Optional.of(holder.get().dataHolderBrands()).orElse(Set.of()), DioDataHolderBrand.class));
  }

  @Override
  public ResponseEntity<DioDataHolderBrand> getHolderBrand(UUID holderId, UUID brandId) {
    Optional<DataHolderBrandData> data = brandRepository.findByIdAndDataHolderId(brandId, holderId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single data holder brand with holder of {} and brand of {} and got content of {}", holderId, brandId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioDataHolderBrand.class));
    } else {
      LOG.warn("Attempted to retrieve a single holder {} with brand of {} and couldn't find it", 
          holderId, brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }  }

  @Override
  public ResponseEntity<DioDataHolderBrand> updateHolderBrand(UUID holderId, UUID brandId,
      DioDataHolderBrand updateData) {
    Optional<DataHolderBrandData> optionalData = brandRepository.findByIdAndDataHolderId(brandId, holderId);

    if (optionalData.isPresent()) {
      DataHolderBrandData data = optionalData.get();
      mapper.map(updateData, data);
      data.dataHolder(optionalData.get().dataHolder());
      DataHolderBrandData updatedData = brandRepository.save(data);

      LOG.info("Updating a single data holder brand of holder {} and brand {} and now set to {}", holderId, brandId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioDataHolderBrand.class));
    } else {
      LOG.warn("Attempted to retrieve a single data holder brand and could not find with holder of {} and brand of {}",
          holderId, brandId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteHolderBrand(UUID holderId, UUID brandId) {
    Optional<DataHolderBrandData> data = brandRepository.findByIdAndDataHolderId(brandId, holderId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data holder brand with id of {}", holderId);
      brandRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to retrieve a single data holder brand and could not find with holder of {} and brand of {}",
          holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
