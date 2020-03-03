package io.biza.heimdall.auth.test.token;

import static org.junit.jupiter.api.Assertions.fail;
import java.net.URI;
import java.util.UUID;
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
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.heimdall.auth.HeimdallAuthApplication;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.thumb.oidc.ClientConfig;
import io.biza.thumb.oidc.OIDCClient;
import io.biza.thumb.oidc.exceptions.TokenAuthorisationFailureException;
import lombok.extern.slf4j.Slf4j;

/**
 * TestClientCredentialsTokenRetrieval
 * 
 * Tests a Token retrieval using Client Credentials
 *
 */
@Slf4j
public class ClientCredentialsTokenIT extends SpringTestEnvironment {

  @Test
  public void testClientCredentialsToken() {
    OIDCClient client = new OIDCClient(
        ClientConfig.builder().issuer(getIssuerUri()).sslContext(trustAllCerts()).build());

    try {
      TokenResponse token = client.tokenClient().getTokens(
          RequestTokenClientCredentials.builder().clientId(TestDataConstants.CLIENT_ID)
              .clientSecret(TestDataConstants.CLIENT_SECRET).build());

      LOG.info("Token retrieval returned: {}", token.toString());

    } catch (TokenAuthorisationFailureException e) {
      LOG.error("Failed to perform client credentials token retrieval", e);
      LOG.error("Error Details are: {}", e.oauth2().toString());
      fail(e.oauth2().toString());
    }


  }

}
