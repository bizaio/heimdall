package io.biza.heimdall.auth.test.token;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.sun.net.httpserver.HttpServer;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.spring.exceptions.KeyRetrievalException;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.SigningVerificationException;
import io.biza.babelfish.spring.service.LocalKeyStoreJWKService;
import io.biza.heimdall.auth.test.Constants;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.thumb.client.Thumb;
import io.biza.thumb.client.ThumbConfig;
import io.biza.thumb.client.ThumbConfigAuth;
import io.biza.thumb.client.ThumbConfigRegister;
import io.biza.thumb.client.enumerations.ThumbAuthMethod;
import io.biza.thumb.client.exceptions.AuthorisationFailure;
import io.biza.thumb.client.exceptions.DiscoveryFailure;
import lombok.extern.slf4j.Slf4j;

/**
 * TestClientCredentialsTokenRetrieval
 * 
 * Tests a Token retrieval using Client Credentials
 *
 */
@Slf4j
public class PrivateKeyJwtTokenIT extends SpringTestEnvironment {

  @Test
  public void testPrivateKeyJwtToken() throws IOException, NotInitialisedException {

    /**
     * Setup Thumb
     */
    /**
     * Thumb client = new Thumb() OIDCClient client = new OIDCClient(
     * ClientConfig.builder().clientId(TestDataConstants.RECIPIENT_CLIENT_ID).issuer(getIssuerUri()).build(),
     * HttpClientUtil.httpClient(false), new LocalKeyStoreJWKService());
     */

    Thumb client = new Thumb(ThumbConfig.builder()
        .register(ThumbConfigRegister.builder()
            .auth(ThumbConfigAuth.builder().clientId(TestDataConstants.RECIPIENT_CLIENT_ID).authMethod(ThumbAuthMethod.PRIVATE_KEY_JWT).build())
            .build())
        .build());
    
    /**
     * Serve the JWKS
     */
    HttpServer httpServer = client.register().auth().serveJwks(TestDataConstants.RECIPIENT_PORT);

    try {
      BearerAccessToken token =
          client.register().auth().accessToken(List.of(io.biza.heimdall.auth.Constants.SECURITY_SCOPE_REGISTER_BANK_READ));
      httpServer.stop(1);

      LOG.info("Token retrieval returned: {}", token.toString());
    } catch (DiscoveryFailure e) {
      fail(e);
    } catch (AuthorisationFailure e) {
      fail(e);
    } finally {
      httpServer.stop(1);
    }
  }

}
