package io.biza.heimdall.register.api.impl;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.common.exceptions.NotFoundException;
import io.biza.babelfish.common.exceptions.NotInitialisedException;
import io.biza.babelfish.common.exceptions.SigningOperationException;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatusV1;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipientV1;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatusV1;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusListV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientListV1;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusListV1;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.spring.service.common.OrikaMapperService;
import io.biza.heimdall.register.api.delegate.DataRecipientApiDelegate;
import io.biza.heimdall.shared.component.service.RecipientService;
import io.biza.heimdall.shared.component.service.SoftwareProductService;
import io.biza.heimdall.shared.persistence.specifications.RecipientBrandSpecifications;
import io.biza.heimdall.shared.persistence.specifications.RecipientSpecifications;
import io.biza.heimdall.shared.persistence.specifications.SoftwareProductSpecifications;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class DataRecipientApiDelegateImpl implements DataRecipientApiDelegate {

	@Autowired
	RecipientService recipientService;

	@Autowired
	SoftwareProductService softwareService;

	@Autowired
	OrikaMapperService mapper;

	@Override
	public ResponseEntity<ResponseRegisterDataRecipientListV1> getBankingDataRecipients(IndustryType industry) {
		return ResponseEntity.ok(ResponseRegisterDataRecipientListV1.builder().data(recipientService
				.list(RecipientSpecifications.industry(industry), null, RegisterDataRecipientV1.class).getContent())
				.build());
	}

	@Override
	public ResponseEntity<DataRecipientsStatusListV1> getBankingDataRecipientStatuses(IndustryType industry) {
		return ResponseEntity.ok(DataRecipientsStatusListV1.builder().dataRecipients(recipientService
				.list(RecipientSpecifications.industry(industry), null, DataRecipientStatusV1.class).getContent())
				.build());
	}

	@Override
	public ResponseEntity<RawJson> getSoftwareStatementAssertion(IndustryType industry, UUID brandId, UUID productId)
			throws SigningOperationException, NotFoundException, NotInitialisedException {
		return ResponseEntity.ok(RawJson.from(softwareService.getSoftwareStatementAssertion(brandId, productId)));
	}

	@Override
	public ResponseEntity<SoftwareProductsStatusListV1> getSoftwareProductStatuses(IndustryType industry) {
		return ResponseEntity.ok(SoftwareProductsStatusListV1.builder()
				.softwareProducts(softwareService
						.list(SoftwareProductSpecifications.industry(industry), null, SoftwareProductStatusV1.class)
						.getContent())
				.build());
	}

}
