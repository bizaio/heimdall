package io.biza.heimdall.register.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrand;
import io.biza.babelfish.cdr.models.responses.register.RequestGetDataHolderBrands;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandList;
import io.biza.heimdall.register.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.component.persistence.HolderBrandService;
import io.biza.heimdall.shared.component.persistence.RecipientService;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.specifications.HolderBrandSpecifications;
import io.biza.heimdall.shared.util.RegisterContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {

  @Autowired
  HolderBrandService holderBrandService;

  @Autowired
  HeimdallMapper mapper;

  @Override
  public ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
      RequestGetDataHolderBrands requestList) {

    Specification<DataHolderBrandData> specification = null;

    if (requestList.updatedSince() != null) {
      specification = HolderBrandSpecifications.updatedSince(requestList.updatedSince());
    }

    Page<DioDataHolderBrand> result = holderBrandService.list(specification,
        PageRequest.of(requestList.page(), requestList.pageSize()));

    return ResponseEntity.ok(ResponseRegisterDataHolderBrandList.builder()
        .meta(RegisterContainerAttributes.toMetaPaginated(result))
        .links(RegisterContainerAttributes.toLinksPaginated(result))
        .data(mapper.mapAsList(result.getContent(), RegisterDataHolderBrand.class)).build());

  }

}
