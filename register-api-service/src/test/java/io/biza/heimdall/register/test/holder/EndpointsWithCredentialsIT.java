package io.biza.heimdall.register.test.holder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.UUID;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrand;
import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientBrandMetaData;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductMetaData;
import io.biza.babelfish.cdr.models.payloads.register.recipient.SoftwareProductStatus;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandList;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.heimdall.register.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.util.JoseSigningUtil;
import io.biza.thumb.client.Thumb;
import io.biza.thumb.client.ThumbConfig;
import io.biza.thumb.client.ThumbConfigRegister;
import io.biza.thumb.client.enumerations.ThumbRegisterMode;
import io.biza.thumb.client.exceptions.AuthorisationFailure;
import io.biza.thumb.client.exceptions.DataRetrievalFailure;
import io.biza.thumb.client.exceptions.ErrorListException;
import io.biza.thumb.oidc.ClientConfig;
import io.biza.thumb.oidc.OIDCClient;
import io.biza.thumb.oidc.exceptions.DiscoveryFailureException;
import io.biza.thumb.oidc.exceptions.TokenAuthorisationFailureException;
import io.biza.thumb.oidc.exceptions.TokenProcessingFailureException;
import io.biza.thumb.oidc.exceptions.TokenVerificationFailureException;
import io.biza.thumb.oidc.util.HttpClientUtil;
import io.biza.thumb.oidc.util.ResolverUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * TestClientCredentialsTokenRetrieval
 * 
 * Tests a Token retrieval using Client Credentials
 *
 */
@Slf4j
public class EndpointsWithCredentialsIT extends SpringTestEnvironment {

  @Test
  public void testListHolders() {

    try {
      List<RegisterDataHolderBrand> brands =
          getHolderThumb().listDataHolderBrands(IndustryType.BANKING, null, null, null);
      LOG.info("Received a brands list of {}", brands.toString());
      assertNotNull(brands, "Brands List returned null");
    } catch (AuthorisationFailure e) {
      fail(
          "Received an authorisation server for list data holder brands brands listing which should be functional",
          e);
    } catch (DataRetrievalFailure e) {
      fail("Encountered a data retrieval failure for list data holder brands brands listing", e);
    } catch (ErrorListException e) {
      fail("Received an exception with a parseable error list response", e);
    }
  }

  @Test
  public void testListDataRecipients() {
    try {
      List<RegisterDataRecipient> recipients =
          getHolderThumb().listDataRecipients(IndustryType.BANKING);
      LOG.info("Received a recipients list of {}", recipients.toString());
      assertNotNull(recipients, "Recipients List returned null");
    } catch (AuthorisationFailure e) {
      fail("Received an authorisation server for list data recipientss which should be functional",
          e);
    } catch (DataRetrievalFailure e) {
      fail("Encountered a data retrieval failure for list data recipientss", e);
    } catch (ErrorListException e) {
      fail("Received an exception with a parseable error list response", e);
    }
  }

  @Test
  public void testListSoftwareProductStatus() {
    try {
      List<SoftwareProductStatus> recipients =
          getHolderThumb().listSoftwareProductStatus(IndustryType.BANKING);
      LOG.info("Received a list of software product statuses of {}", recipients.toString());
      assertNotNull(recipients, "Software Product Status List returned null");
    } catch (AuthorisationFailure e) {
      fail("Received an authorisation server for list software products which should be functional",
          e);
    } catch (DataRetrievalFailure e) {
      fail("Encountered a data retrieval failure for list software products", e);
    } catch (ErrorListException e) {
      fail("Received an exception with a parseable error list response", e);
    }
  }


  @Test
  public void testSoftwareStatement() {
    try {
      List<RegisterDataRecipient> recipients =
          getHolderThumb().listDataRecipients(IndustryType.BANKING);
      LOG.info("Received a recipients list of {}", recipients.toString());
      assertNotNull(recipients, "Recipients List returned null");

      for (RegisterDataRecipient recipient : recipients) {
        if (recipient.dataRecipientBrands() != null) {
          for (DataRecipientBrandMetaData brand : recipient.dataRecipientBrands()) {
            if (brand.softwareProducts() != null) {
              for (SoftwareProductMetaData product : brand.softwareProducts()) {
                LOG.info("SSA for brand {} and software {} is: {}", brand.dataRecipientBrandId(),
                    product.softwareProductId(),
                    getHolderThumb().getSoftwareStatementAssertion(IndustryType.BANKING,
                        brand.dataRecipientBrandId(), product.softwareProductId()));
              }
            }
          }
        }
      }
    } catch (AuthorisationFailure e) {
      fail("Received an authorisation server for test software statement", e);
    } catch (DataRetrievalFailure e) {
      fail("Encountered a data retrieval failure for test software statement", e);
    } catch (ErrorListException e) {
      fail("Received an exception with a parseable error list response", e);
    }
  }
}
