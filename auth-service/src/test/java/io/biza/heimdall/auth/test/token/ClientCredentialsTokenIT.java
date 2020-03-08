package io.biza.heimdall.auth.test.token;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.IOException;
import java.util.List;
import org.junit.Test;
import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import com.sun.net.httpserver.HttpServer;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.spring.exceptions.KeyRetrievalException;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.exceptions.SigningOperationException;
import io.biza.babelfish.spring.exceptions.SigningVerificationException;
import io.biza.babelfish.spring.service.LocalKeyStoreJWKService;
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
public class ClientCredentialsTokenIT extends SpringTestEnvironment {

  @Test
  public void testClientCredentialsToken() throws IOException, NotInitialisedException  {
    Thumb client = new Thumb(ThumbConfig.builder()
        .register(ThumbConfigRegister.builder()
            .auth(ThumbConfigAuth.builder().authMethod(ThumbAuthMethod.CLIENT_CREDENTIALS).clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).clientId(TestDataConstants.HOLDER_CLIENT_ID).build())
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
