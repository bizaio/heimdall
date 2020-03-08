package io.biza.heimdall.auth.test.discovery;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.Test;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.thumb.client.Thumb;
import io.biza.thumb.client.ThumbConfig;
import io.biza.thumb.client.ThumbConfigAuth;
import io.biza.thumb.client.ThumbConfigRegister;
import io.biza.thumb.client.enumerations.ThumbAuthMethod;
import io.biza.thumb.client.exceptions.DiscoveryFailure;
import lombok.extern.slf4j.Slf4j;

/**
 * TestDiscoveryRetrieval
 * 
 * Tests OpenID Discovery operations
 *
 */
@Slf4j
public class DiscoveryRetrievalIT extends SpringTestEnvironment {

  @Test
  public void testDocumentDiscoveryIsPossible() {
    LOG.info("Issuer URI is set to: {}", getIssuerUri());
    
    Thumb client = new Thumb(ThumbConfig.builder()
        .register(ThumbConfigRegister.builder()
            .auth(ThumbConfigAuth.builder().authMethod(ThumbAuthMethod.CLIENT_CREDENTIALS).clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET).clientId(TestDataConstants.HOLDER_CLIENT_ID).build())
            .build())
        .build());


      
      try {
        OIDCProviderMetadata meta = client.register().auth().discovery();
        assertNotNull(meta);
        LOG.info(meta.toString());
      } catch (DiscoveryFailure e) {
        fail("Encountered discovery failure", e);
      }
  }

}
