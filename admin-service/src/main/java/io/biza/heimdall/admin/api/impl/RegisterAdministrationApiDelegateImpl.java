package io.biza.heimdall.admin.api.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;
import io.biza.heimdall.payload.enumerations.JWKStatus;
import io.biza.heimdall.payload.recipient.RegisterDataRecipient;
import io.biza.heimdall.shared.component.mapper.HeimdallMapper;
import io.biza.heimdall.shared.enumerations.HeimdallExceptionType;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
import io.biza.heimdall.shared.payloads.dio.DioRegisterJWK;
import io.biza.heimdall.shared.payloads.requests.dio.RequestJwkCreate;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.RegisterJWKData;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.RegisterJWKRepository;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class RegisterAdministrationApiDelegateImpl implements RegisterAdministrationApiDelegate {

  @Autowired
  RegisterJWKRepository jwkRepository;

  @Autowired
  private HeimdallMapper mapper;

  @Override
  public ResponseEntity<DioRegisterJWK> createJwk(RequestJwkCreate jwkRequest)
      throws JoseException {
    UUID keyIdentifier = UUID.randomUUID();
    RsaJsonWebKey webKey = RsaJwkGenerator.generateJwk(2048);
    webKey.setKeyId(keyIdentifier.toString());
    RegisterJWKData registerJwkData = jwkRepository.save(RegisterJWKData.builder().id(keyIdentifier)
        .jwk(webKey.toJson(OutputControlLevel.INCLUDE_PRIVATE)).status(JWKStatus.ACTIVE).build());
    return ResponseEntity.ok(mapper.map(registerJwkData, DioRegisterJWK.class));
  }

}
