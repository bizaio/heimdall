package io.biza.heimdall.register.api.impl;

import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatus;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatus;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.util.PrimitiveUtil;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.service.RecipientService;
import io.biza.heimdall.shared.component.service.SoftwareProductService;
import io.biza.heimdall.shared.component.support.HeimdallMapper;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class BankingDataRecipientApiDelegateImpl implements BankingDataRecipientApiDelegate {

	@Autowired
	RecipientService recipientService;

	@Autowired
	SoftwareProductService softwareService;

	@Autowired
	HeimdallMapper mapper;

	@Override
	public ResponseEntity<ResponseRegisterDataRecipientList> getBankingDataRecipients() {
		return ResponseEntity.ok(ResponseRegisterDataRecipientList.builder()
				.data(Optional.ofNullable(
						mapper.mapAsList(recipientService.list(null, null).toList(), RegisterDataRecipient.class))
						.orElse(PrimitiveUtil.emptyList()))
				.build());
	}

	@Override
	public ResponseEntity<DataRecipientsStatusList> getBankingDataRecipientStatuses() {
		return ResponseEntity
				.ok(DataRecipientsStatusList.builder()
						.dataRecipients(Optional.ofNullable(
								mapper.mapAsList(recipientService.list(null, null).toList(), DataRecipientStatus.class))
								.orElse(PrimitiveUtil.emptyList()))
						.build());
	}

	@Override
	public ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId, UUID productId)
			throws SigningOperationException, NotFoundException, NotInitialisedException {
		return ResponseEntity.ok(RawJson.from(softwareService.getSoftwareStatementAssertion(brandId, productId)));
	}

	@Override
	public ResponseEntity<SoftwareProductsStatusList> getSoftwareProductStatuses() {
		return ResponseEntity.ok(SoftwareProductsStatusList.builder()
				.softwareProducts(Optional
						.ofNullable(mapper.mapAsList(softwareService.list(null, null), SoftwareProductStatus.class))
						.orElse(PrimitiveUtil.emptyList()))
				.build());
	}

}
