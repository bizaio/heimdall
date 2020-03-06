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
import java.util.List;
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
import io.biza.heimdall.auth.test.Constants;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
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
public class PrivateKeyJwtTokenIT extends SpringTestEnvironment {

  @Test
  public void testPrivateKeyJwtToken() throws DiscoveryFailureException, NoSuchAlgorithmException, JoseException, IOException {

      /**
       * Generate a new key pair
       */
      KeyPairGenerator keyGen = KeyPairGenerator.getInstance(Constants.KEYPAIR_ALGORITHM);
      keyGen.initialize(2048);
      KeyPair keyPair = keyGen.generateKeyPair();

      /**
       * JWKS Setup
       */

      PublicJsonWebKey webKey = PublicJsonWebKey.Factory.newPublicJwk(keyPair.getPublic());
      webKey.setAlgorithm(AlgorithmIdentifiers.RSA_PSS_USING_SHA256);
      webKey.setKeyId(UUID.randomUUID().toString());
      webKey.setPrivateKey(keyPair.getPrivate());

      /**
       * Print the keys out
       */
      LOG.info("Generated private key is: {}", Base64.encode(keyPair.getPrivate().getEncoded()));
      LOG.info("Generated public key is: {}", Base64.encode(webKey.getPublicKey().getEncoded()));

      /**
       * Setup OIDC Client
       */
      OIDCClient client = new OIDCClient(
          ClientConfig.builder().signingKeyPair(webKey).clientId(TestDataConstants.RECIPIENT_CLIENT_ID).issuer(getIssuerUri()).build(), HttpClientUtil.httpClient(false));


      /**
       * Construct a JWKS
       */
      JsonWebKeySet jsonWebKeySet = new JsonWebKeySet();
      jsonWebKeySet.addJsonWebKey(webKey);

      /**
       * Serve the JWKS
       */
      HttpServer httpServer =
          JWKSWebServerUtil.serveJwks(TestDataConstants.RECIPIENT_PORT, jsonWebKeySet.toJson());

      try {
        TokenResponse token = client.tokens(List.of(io.biza.heimdall.auth.Constants.SECURITY_SCOPE_REGISTER_BANK_READ));
        httpServer.stop(1);

        LOG.info("Token retrieval returned: {}", token.toString());
      } catch (TokenAuthorisationFailureException e) {
        LOG.error("Failed to perform client credentials token retrieval", e);
        if(e.oauth2() != null) {
          LOG.error("Error Details are: {}", e.oauth2().toString());
        }
        fail(e);
      } catch (TokenVerificationFailureException e) {
        LOG.error("Received token but was unable to verify it's signature", e);
        fail(e);
      } catch (TokenProcessingFailureException e) {
        LOG.error("Failed to process data after token retrieval", e);
        fail(e);
      } finally {
        httpServer.stop(1);
      }
  }

}
