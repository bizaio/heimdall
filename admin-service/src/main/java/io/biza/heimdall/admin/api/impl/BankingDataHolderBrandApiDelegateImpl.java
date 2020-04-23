package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderBrandApiDelegate;
import io.biza.heimdall.shared.component.service.HolderBrandService;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.specifications.HolderBrandSpecifications;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderBrandApiDelegateImpl implements BankingDataHolderBrandApiDelegate {

  @Autowired
  HolderBrandService holderBrand;
  
  @Autowired
  OrikaMapperService mapper;

  @Override
  public ResponseEntity<DioDataHolderBrand> createHolderBrand(UUID holderId,
      DioDataHolderBrand brand) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(holderBrand.create(holderId, brand));
  }

  @Override
  public ResponseEntity<List<DioDataHolderBrand>> listHolderBrands(UUID holderId) {
    return ResponseEntity.ok(holderBrand.list(HolderBrandSpecifications.holderId(holderId), null, DioDataHolderBrand.class).toList());
  }

  @Override
  public ResponseEntity<DioDataHolderBrand> getHolderBrand(UUID holderId, UUID brandId) throws NotFoundException {
    return ResponseEntity.ok(holderBrand.read(holderId, brandId));
  }

  @Override
  public ResponseEntity<DioDataHolderBrand> updateHolderBrand(UUID holderId, UUID brandId,
      DioDataHolderBrand updateData) throws ValidationListException, NotFoundException {
    return ResponseEntity.ok(holderBrand.update(holderId, brandId, updateData));
  }

  @Override
  public ResponseEntity<Void> deleteHolderBrand(UUID holderId, UUID brandId) throws NotFoundException {
    holderBrand.delete(holderId, brandId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
