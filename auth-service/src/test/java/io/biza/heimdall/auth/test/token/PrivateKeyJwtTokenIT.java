package io.biza.heimdall.auth.test.token;

import static org.junit.jupiter.api.Assertions.fail;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jose4j.base64url.Base64;
import org.jose4j.jwk.JsonWebKeySet;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import org.junit.jupiter.api.Test;
import com.sun.net.httpserver.HttpServer;
import io.biza.babelfish.oidc.enumerations.OAuth2ClientAssertionType;
import io.biza.babelfish.oidc.enumerations.OAuth2GrantType;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.babelfish.oidc.requests.RequestTokenPrivateKeyJwt;
import io.biza.babelfish.oidc.util.JWKSWebServerUtil;
import io.biza.babelfish.oidc.util.SigningUtil;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
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
public class PrivateKeyJwtTokenIT extends SpringTestEnvironment {

  public final String ALGORITHM = "RSASSA-PSS";

  @Test
  public void testPrivateKeyJwtToken() {
    OIDCClient client = new OIDCClient(
        ClientConfig.builder().issuer(getIssuerUri()).sslContext(trustAllCerts()).build());
    
    try {
      client.discoveryClient().getDiscoveryDocument(true);
    } catch (DiscoveryFailureException e) {
      fail("Discovery failure: ", e);
    }

    try {
      /**
       * Generate a new key pair
       */
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(2048);
      KeyPair keyPair = keyGen.generateKeyPair();

      /**
       * JWKS Setup
       */

      PublicJsonWebKey webKey = PublicJsonWebKey.Factory.newPublicJwk(keyPair.getPublic());
      webKey.setAlgorithm(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
      webKey.setKeyId(UUID.randomUUID().toString());
      webKey.setPrivateKey(keyPair.getPrivate());
      client.config().signingKeyPair(webKey);

      /**
       * Print the keys out
       */
      LOG.info("Generated private key is: {}", Base64.encode(keyPair.getPrivate().getEncoded()));
      LOG.info("Generated public key is: {}", Base64.encode(webKey.getPublicKey().getEncoded()));

      /**
       * Construct a JWKS
       */
      JsonWebKeySet jsonWebKeySet = new JsonWebKeySet();
      jsonWebKeySet.addJsonWebKey(webKey);

      /**
       * Set the signing key pair
       */
      client.config().signingKeyPair(webKey);

      /**
       * Serve the JWKS
       */
      HttpServer httpServer =
          JWKSWebServerUtil.serveJwks(TestDataConstants.RECIPIENT_PORT, jsonWebKeySet.toJson());

      try {
        TokenResponse token = client.tokenClient().getTokens(
            RequestTokenPrivateKeyJwt.builder().clientId(TestDataConstants.RECIPIENT_CLIENT_ID).build());
        httpServer.stop(1);

        LOG.info("Token retrieval returned: {}", token.toString());
        
        try {
          LOG.info("Retrieving issuers jwks from {}", client.config().discoveryMetadata().jwksUri());
          HttpRequest request = HttpRequest.newBuilder().GET().uri(client.config().discoveryMetadata().jwksUri())
              .setHeader("User-Agent", "Biza.io Babelfish OIDC").build();
          HttpResponse<String> jwksContent =
              client.httpClient().send(request, HttpResponse.BodyHandlers.ofString());

          JsonWebKeySet jwks = new JsonWebKeySet(jwksContent.body());

          SigningUtil.verify(token.accessToken(), jwks, client.config().issuer().toString(), TestDataConstants.RECIPIENT_CLIENT_ID);
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
        LOG.error("Failed to perform private key jwt retrieval", e);
        if (e.oauth2() != null) {
          LOG.error("Error Details are: {}", e.oauth2().toString());
        }
        // Cleanup the webserver
        httpServer.stop(5);
        fail("Token Authorisation with Private Key JWT didn't success", e);
      }

      /**
       * Shutdown the http server
       */
      httpServer.stop(1);

    } catch (JoseException e) {
      fail("Encountered JOSE Exception while doing setup", e);
    } catch (NoSuchAlgorithmException e) {
      fail("Algorithm not found: ", e);
    } catch (IOException e) {
      fail("Encountered IOException while attempting to start", e);
    }
  }

}
