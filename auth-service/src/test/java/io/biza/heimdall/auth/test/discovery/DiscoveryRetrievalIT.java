package io.biza.heimdall.auth.test.discovery;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import java.net.URI;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.test.Constants;
import io.biza.heimdall.auth.test.SpringTestEnvironment;
import io.biza.thumb.oidc.ClientConfig;
import io.biza.thumb.oidc.OIDCClient;
import io.biza.thumb.oidc.exceptions.DiscoveryFailureException;
import io.biza.thumb.oidc.util.HttpClientUtil;
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
  public void testDocumentDiscoveryIsPossible() throws Exception {
    LOG.info("Issuer URI is set to: {}", getIssuerUri());

    OIDCClient client = new OIDCClient(
        ClientConfig.builder().issuer(getIssuerUri()).build(), HttpClientUtil.httpClient(false));
    try {
      ProviderDiscoveryMetadata meta = client.metadata();
      assertNotNull(meta);
      LOG.info(meta.toString());
    } catch (DiscoveryFailureException e) {
      fail("Discovery failure: " + e.toString(), e);
    }
  }

}
