package io.biza.heimdall.auth.test.token;

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
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.heimdall.auth.HeimdallAuthApplication;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.auth.util.EndpointUtil;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import io.biza.heimdall.shared.persistence.repository.ClientRepository;
import io.biza.heimdall.shared.persistence.repository.DataHolderRepository;
import io.biza.heimdall.shared.util.JoseSigningUtil;
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
public class ClientCredentialsTokenIT extends SpringTestEnvironment {

  @Test
  public void testClientCredentialsToken() throws DiscoveryFailureException {
    OIDCClient client = new OIDCClient(
        ClientConfig.builder().issuer(getIssuerUri()).clientId(TestDataConstants.HOLDER_CLIENT_ID)
            .clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).build(),
        HttpClientUtil.httpClient(false));

    try {
      TokenResponse token =
          client.tokens(List.of(io.biza.heimdall.auth.Constants.SECURITY_SCOPE_REGISTER_BANK_READ));
      LOG.info("Token retrieval returned: {}", token.toString());

    } catch (TokenAuthorisationFailureException e) {
      LOG.error("Failed to perform client credentials token retrieval", e);
      if (e.oauth2() != null) {
        LOG.error("Error Details are: {}", e.oauth2().toString());
      }
      fail(e);
    } catch (TokenVerificationFailureException e) {
      LOG.error("Received token but it failed verification", e);
      fail(e);
    } catch (TokenProcessingFailureException e) {
      LOG.error("Received token but it was unable to be processed", e);
      fail(e);
    }


  }

}
