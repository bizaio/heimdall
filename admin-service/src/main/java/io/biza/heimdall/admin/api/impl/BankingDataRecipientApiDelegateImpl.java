package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  private HeimdallMapper mapper;
  
  @Override
  public ResponseEntity<DioDataRecipient> createRecipient(DioDataRecipient recipient) {
    DataRecipientData dataRecipientData = mapper.map(recipient, DataRecipientData.class);
    DataRecipientData savedDataRecipient = recipientRepository.save(dataRecipientData);
    LOG.debug("Created a new data recipient with content of: {}", savedDataRecipient);
    return ResponseEntity.ok(mapper.map(savedDataRecipient, DioDataRecipient.class));
  }

  @Override
  public ResponseEntity<List<DioDataRecipient>> listRecipients() {
    List<DataRecipientData> dataRecipientData = recipientRepository.findAll();
    LOG.debug("Listing all data recipients and received {}", dataRecipientData);
    return ResponseEntity.ok(mapper.mapAsList(dataRecipientData, DioDataRecipient.class));
  }

  @Override
  public ResponseEntity<DioDataRecipient> getRecipient(UUID recipientId) {
    Optional<DataRecipientData> data = recipientRepository.findById(recipientId);

    if (data.isPresent()) {
      LOG.info("Retrieving a single data recipient with id of {} and got content of {}", recipientId,
          data.get());
      return ResponseEntity.ok(mapper.map(data.get(), DioDataRecipient.class));
    } else {
      LOG.warn("Attempted to retrieve a single data recipient and could not find with id of {}",
          recipientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<DioDataRecipient> updateRecipient(UUID recipientId,
      DioDataRecipient updateData) {
    Optional<DataRecipientData> optionalData = recipientRepository.findById(recipientId);

    if (optionalData.isPresent()) {
      DataRecipientData data = optionalData.get();
      mapper.map(updateData, data);
      DataRecipientData updatedData = recipientRepository.save(data);

      LOG.info("Updating a single data recipient of id {} and now set to {}", recipientId, updatedData);

      return ResponseEntity.ok(mapper.map(updatedData, DioDataRecipient.class));
    } else {
      LOG.warn("Attempted to retrieve a single data recipient and could not find with id of {}",
          recipientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @Override
  public ResponseEntity<Void> deleteRecipient(UUID recipientId) {
    Optional<DataRecipientData> data = recipientRepository.findById(recipientId);

    if (data.isPresent()) {
      LOG.info("Deleting a single data recipient with id of {}", recipientId);
      recipientRepository.delete(data.get());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } else {
      LOG.warn("Attempted to retrieve a single data recipient and could not find with id of {}",
          recipientId);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }
}
