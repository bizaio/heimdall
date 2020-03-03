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
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.specifications.DataHolderBrandSpecifications;
import io.biza.heimdall.shared.util.RegisterContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataHolderApiDelegateImpl implements BankingDataHolderApiDelegate {

  @Autowired
  DataHolderBrandRepository holderRepository;

  @Autowired
  HeimdallMapper mapper;

  @Override
  public ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
      RequestGetDataHolderBrands requestList) {

    Specification<DataHolderBrandData> specification = null;

    if (requestList.updatedSince() != null) {
      specification = DataHolderBrandSpecifications.updatedSince(requestList.updatedSince());
    }

    Page<DataHolderBrandData> dataHolderBrandData = holderRepository.findAll(specification,
        PageRequest.of(requestList.page() - 1, requestList.pageSize()));

    ResponseRegisterDataHolderBrandList listResponse = ResponseRegisterDataHolderBrandList.builder()
        .meta(RegisterContainerAttributes.toMetaPaginated(dataHolderBrandData))
        .links(RegisterContainerAttributes.toLinksPaginated(dataHolderBrandData))
        .data(mapper.mapAsList(dataHolderBrandData.getContent(), RegisterDataHolderBrand.class))
        .build();
    LOG.debug("List response came back with: {}", listResponse);
    return ResponseEntity.ok(listResponse);
  }

}
