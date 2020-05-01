package io.biza.heimdall.register.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrandV1;
import io.biza.babelfish.cdr.models.requests.register.RequestGetDataHolderBrandsV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandListV1;
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.heimdall.register.api.delegate.DataHolderApiDelegate;
import io.biza.heimdall.shared.component.service.HolderBrandService;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.specifications.HolderBrandSpecifications;
import io.biza.heimdall.shared.util.RegisterContainerAttributes;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class DataHolderApiDelegateImpl implements DataHolderApiDelegate {

	@Autowired
	HolderBrandService holderBrandService;

	@Autowired
	OrikaMapperService mapper;

	@Override
	public ResponseEntity<ResponseRegisterDataHolderBrandListV1> getBankingDataHolderBrands(
			RequestGetDataHolderBrandsV1 request, PageRequest pageRequest) {

		Specification<DataHolderBrandData> specification = HolderBrandSpecifications.industry(request.industryType());
		
		if (request.updatedSince() != null) {
			specification = specification.and(HolderBrandSpecifications.updatedSince(request.updatedSince()));
		}

		Page<RegisterDataHolderBrandV1> result = holderBrandService.list(specification, pageRequest,
				RegisterDataHolderBrandV1.class);

		LOG.debug("Received holder brand response of: {}", result.getContent());

		return ResponseEntity.ok(ResponseRegisterDataHolderBrandListV1.builder()
				.meta(RegisterContainerAttributes.toMetaPaginated(result))
				.links(RegisterContainerAttributes.toLinksPaginated(result)).data(result.getContent()).build());

	}

}
