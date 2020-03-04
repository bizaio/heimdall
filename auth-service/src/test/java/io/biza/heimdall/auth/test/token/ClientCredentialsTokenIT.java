package io.biza.heimdall.auth.test.token;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
import io.biza.babelfish.oidc.util.SigningUtil;
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
import io.biza.thumb.oidc.ClientConfig;
import io.biza.thumb.oidc.OIDCClient;
import io.biza.thumb.oidc.exceptions.DiscoveryFailureException;
import io.biza.thumb.oidc.exceptions.TokenAuthorisationFailureException;
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
  public void testClientCredentialsToken() {
    OIDCClient client = new OIDCClient(
        ClientConfig.builder().issuer(getIssuerUri()).sslContext(trustAllCerts()).build());
    
    try {
      client.discoveryClient().getDiscoveryDocument(true);
    } catch (DiscoveryFailureException e) {
      fail("Discovery failure: ", e);
    }

    try {
      TokenResponse token = client.tokenClient().getTokens(
          RequestTokenClientCredentials.builder().clientId(TestDataConstants.HOLDER_CLIENT_ID)
              .clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).build());

      LOG.info("Token retrieval returned: {}", token.toString());
      
      try {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(client.config().discoveryMetadata().jwksUri())
            .setHeader("User-Agent", "Biza.io Babelfish OIDC").build();
        HttpResponse<String> jwksContent =
            client.httpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JsonWebKeySet jwks = new JsonWebKeySet(jwksContent.body());

        SigningUtil.verify(token.accessToken(), jwks, client.config().issuer().toString(), TestDataConstants.HOLDER_CLIENT_ID);
      } catch (JoseException e) {
        LOG.error("Encountered Generic Jose4j Exception", e);
        fail("Generic Jose4j Exception", e);
      } catch (InvalidJwtException e) {
        LOG.warn("Generic Jose4j Exception", e);
        fail("Generic Jose4j Exception", e);
      } catch (IOException | InterruptedException e) {
        LOG.error("Encountered generic signing verification error", e);
        fail("Encountered generic signing verification error", e);
      }
      

    } catch (TokenAuthorisationFailureException e) {
      LOG.error("Failed to perform client credentials token retrieval", e);
      LOG.error("Error Details are: {}", e.oauth2().toString());
      fail(e.oauth2().toString());
    }


  }

}
