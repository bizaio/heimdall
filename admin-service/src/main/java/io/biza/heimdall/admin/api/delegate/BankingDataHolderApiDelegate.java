package io.biza.heimdall.admin.api.delegate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

import io.biza.babelfish.cdr.exceptions.NotFoundException;
import io.biza.babelfish.cdr.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;

public interface BankingDataHolderApiDelegate {
  default Optional<NativeWebRequest> getRequest() {
    return Optional.empty();
  }

  default ResponseEntity<DioDataHolder> createHolder(DioDataHolder holder) throws ValidationListException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<List<DioDataHolder>> listHolders() {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolder> getHolder(UUID holderId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<DioDataHolder> updateHolder(UUID holderId, DioDataHolder holder) throws ValidationListException, NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

  default ResponseEntity<Void> deleteHolder(UUID holderId) throws NotFoundException {
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }

}
