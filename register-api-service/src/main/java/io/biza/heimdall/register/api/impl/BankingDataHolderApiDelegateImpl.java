package io.biza.heimdall.register.api.impl;

import java.util.Optional;

import org.apache.commons.collections.ListUtils;
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
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.babelfish.spring.util.PrimitiveUtil;
import io.biza.heimdall.register.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.shared.component.service.HolderBrandService;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
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
	  OrikaMapperService mapper;

	@Override
	public ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
			RequestGetDataHolderBrands requestList) {

		Specification<DataHolderBrandData> specification = null;

		if (requestList.updatedSince() != null) {
			specification = HolderBrandSpecifications.updatedSince(requestList.updatedSince());
		}

		Page<RegisterDataHolderBrand> result = holderBrandService.list(specification,
				PageRequest.of(requestList.page()-1, requestList.pageSize()), RegisterDataHolderBrand.class);
		
		LOG.debug("Received holder brand response of: {}", result.getContent());

		return ResponseEntity.ok(
				ResponseRegisterDataHolderBrandList.builder().meta(RegisterContainerAttributes.toMetaPaginated(result))
						.links(RegisterContainerAttributes.toLinksPaginated(result))
						.data(result.getContent())
						.build());

	}

}
