package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {

  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioDataHolder> createHolder(DioDataHolder holder) {
    DataHolderData dataHolderData = mapper.map(holder, DataHolderData.class);
    DataHolderData savedDataHolder = holderRepository.save(dataHolderData);
    LOG.debug("Created a new data holder with content of: {}", savedDataHolder);
    return ResponseEntity.ok(mapper.map(savedDataHolder, DioDataHolder.class));
  }

  @Override
  public ResponseEntity<List<DioDataHolder>> listHolders() {
    List<DataHolderData> dataHolderData = holderRepository.findAll();
    LOG.debug("Listing all data holders and received {}", dataHolderData);
    return ResponseEntity.ok(mapper.mapAsList(dataHolderData, DioDataHolder.class));
  }

  @Override
  public ResponseEntity<DioDataHolder> getHolder(UUID holderId) {
    Optional<DataHolderData> data = holderRepository.findById(holderId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single data holder with id of {} and got content of {}", holderId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioDataHolder.class));
    } else {
      LOG.warn("Attempted to retrieve a single data holder and could not find with id of {}",
          holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioDataHolder> updateHolder(UUID holderId, DioDataHolder updateData) {
    Optional<DataHolderData> optionalData = holderRepository.findById(holderId);

    if (optionalData.isPresent()) {
      DataHolderData data = optionalData.get();
      mapper.map(updateData, data);
      DataHolderData updatedData = holderRepository.save(data);

      LOG.info("Updating a single data holder of id {} and now set to {}", holderId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioDataHolder.class));
    } else {
      LOG.warn("Attempted to retrieve a single data holder and could not find with id of {}",
          holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteHolder(UUID holderId) {
    Optional<DataHolderData> data = holderRepository.findById(holderId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data holder with id of {}", holderId);
      holderRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to retrieve a single data holder and could not find with id of {}",
          holderId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
