package io.biza.heimdall.register.api.impl;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatus;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatus;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusList;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientList;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusList;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.payloads.JWTClaims;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.interfaces.OldJWKService;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.shared.component.service.RecipientService;
import io.biza.heimdall.shared.component.service.SoftwareProductService;
import io.biza.heimdall.shared.component.support.HeimdallMapper;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
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
    return ResponseEntity.ok(ResponseRegisterDataRecipientList.builder().data(
        mapper.mapAsList(recipientService.list(null, null).toList(), RegisterDataRecipient.class))
        .build());
  }

  @Override
  public ResponseEntity<DataRecipientsStatusList> getBankingDataRecipientStatuses() {
    return ResponseEntity.ok(DataRecipientsStatusList.builder()
        .dataRecipients(
            mapper.mapAsList(recipientService.list(null, null).toList(), DataRecipientStatus.class))
        .build());
  }

  @Override
  public ResponseEntity<RawJson> getSoftwareStatementAssertion(UUID brandId, UUID productId)
      throws SigningOperationException, NotFoundException {
    return ResponseEntity
        .ok(RawJson.from(softwareService.getSoftwareStatementAssertion(brandId, productId)));
  }

  @Override
  public ResponseEntity<SoftwareProductsStatusList> getSoftwareProductStatuses() {
    return ResponseEntity.ok(SoftwareProductsStatusList.builder()
        .softwareProducts(
            mapper.mapAsList(softwareService.list(null, null), SoftwareProductStatus.class))
        .build());
  }

}
