/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *******************************************************************************/
package io.biza.heimdall.shared.loaders;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.cdr.enumerations.register.CDRVersionType;
import io.biza.babelfish.cdr.enumerations.register.DataHolderStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientBrandStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.cdr.enumerations.register.RegisterAuthType;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandAuthData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandEndpointData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.repository.SoftwareProductRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientBrandRepository;
import io.biza.heimdall.shared.persistence.repository.DataRecipientRepository;
import io.biza.heimdall.shared.persistence.repository.LegalEntityRepository;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Profile({"test", "dev", "demo"})
public class TestDataSetup implements ApplicationListener<ApplicationReadyEvent> {
  @Autowired
  DataHolderRepository holderRepository;

  @Autowired
  DataHolderBrandRepository holderBrandRepository;

  @Autowired
  DataRecipientRepository recipientRepository;

  @Autowired
  DataRecipientBrandRepository recipientBrandRepository;

  @Autowired
  SoftwareProductRepository productRepository;

  @Autowired
  LegalEntityRepository legalRepository;

  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    if (!holderRepository.existsByName(TestDataConstants.HOLDER_NAME)) {
      LOG.warn("Loading Test Data Holder information");
      DataHolderData holder = holderRepository.save(DataHolderData.builder()
          .industry(IndustryType.BANKING)
          .legalEntity(LegalEntityData.builder().legalName(TestDataConstants.HOLDER_LEGAL_NAME)
              .organisationType(CommonOrganisationType.COMPANY).build())
          .dataHolderBrands(Set.of(DataHolderBrandData.builder()
              .id(UUID.fromString(TestDataConstants.HOLDER_BRAND_ID))
              .authDetails(Set.of(DataHolderBrandAuthData.builder()
                  .authType(RegisterAuthType.HYBRIDFLOW_JWKS)
                  .jwksEndpoint(URI.create("https://localhost:" + TestDataConstants.HOLDER_AUTH_PORT
                      + TestDataConstants.HOLDER_AUTH_JWKS_PATH))
                  .build()))
              .brandName(TestDataConstants.HOLDER_BRAND_NAME).lastUpdated(OffsetDateTime.now())
              .logoUri(TestDataConstants.HOLDER_BRAND_LOGO_URI).status(DataHolderStatusType.ACTIVE)
              .endpointDetail(DataHolderBrandEndpointData.builder()
                  .infosecBaseUri(TestDataConstants.HOLDER_BRAND_INFOSEC_URI)
                  .resourceBaseUri(TestDataConstants.HOLDER_BRAND_RESOURCE_URI)
                  .publicBaseUri(TestDataConstants.HOLDER_BRAND_PUBLIC_URI)
                  .version(CDRVersionType.V1).websiteUri(TestDataConstants.HOLDER_BRAND_WEBSITE)
                  .build())
              .build()))
          .name(TestDataConstants.HOLDER_NAME).build());

      LOG.info("Loaded holder as {}", holder);

    }

    if(!legalRepository.existsByLegalName(TestDataConstants.RECIPIENT_LEGAL_NAME)) {
      LOG.warn("Loading Test Data Recipient information");



      DataRecipientData recipientData = DataRecipientData.builder().industry(IndustryType.BANKING)
          .logoUri(TestDataConstants.RECIPIENT_LOGO_URI).status(DataRecipientStatusType.ACTIVE)
          .build();

      LegalEntityData legalEntity = LegalEntityData.builder()
          .legalName(TestDataConstants.RECIPIENT_LEGAL_NAME)
          .organisationType(CommonOrganisationType.COMPANY).build().dataRecipient(recipientData);
      recipientData.legalEntity(legalEntity);

      DataRecipientData recipient = recipientRepository.save(recipientData);

      LOG.info("Loaded recipient as {}", recipient);

      DataRecipientBrandData recipientBrandData =
          DataRecipientBrandData.builder().brandName(TestDataConstants.RECIPIENT_NAME)
              .id(UUID.fromString(TestDataConstants.RECIPIENT_BRAND_ID))
              .logoUri(TestDataConstants.RECIPIENT_BRAND_LOGO_URI)
              .status(DataRecipientBrandStatusType.ACTIVE).build().dataRecipient(recipient);

      LOG.info("Attempting to save brand data of {}", recipientBrandData);

      DataRecipientBrandData brand = recipientBrandRepository.save(recipientBrandData);

      SoftwareProductData product = productRepository.save(SoftwareProductData.builder()
          .jwksUri(URI.create("http://localhost:" + TestDataConstants.RECIPIENT_PORT
              + TestDataConstants.RECIPIENT_JWKS_PATH))
          .id(UUID.fromString(TestDataConstants.RECIPIENT_PRODUCT_ID))
          .name(TestDataConstants.RECIPIENT_PRODUCT_NAME)
          .uri(TestDataConstants.RECIPIENT_PRODUCT_WEBSITE)
          .description(TestDataConstants.RECIPIENT_PRODUCT_DESCRIPTION)
          .logoUri(TestDataConstants.RECIPIENT_PRODUCT_LOGO_URI)
          .tosUri(TestDataConstants.RECIPIENT_PRODUCT_TOS)
          .policyUri(TestDataConstants.RECIPIENT_PRODUCT_POLICY)
          .revocationUri(TestDataConstants.RECIPIENT_PRODUCT_REVOCATION_URI)
          .scopes(Set.of(CDRScope.BANK_ACCOUNTS_BASIC_READ, CDRScope.BANK_ACCOUNTS_DETAIL_READ))
          .redirectUris(TestDataConstants.RECIPIENT_PRODUCT_REDIRECT_URI).build()
          .dataRecipientBrand(brand));

      LOG.info("Saved: {}", product);
      LOG.info("Test Data Setup Completed");

    }
  }
}
