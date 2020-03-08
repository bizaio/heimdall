package io.biza.heimdall.register.test;

import java.net.URI;
import io.biza.heimdall.shared.TestDataConstants;
import io.biza.thumb.client.Thumb;
import io.biza.thumb.client.ThumbConfig;
import io.biza.thumb.client.ThumbConfigAuth;
import io.biza.thumb.client.ThumbConfigRegister;
import io.biza.thumb.client.enumerations.ThumbAuthMethod;
import io.biza.thumb.client.enumerations.ThumbRegisterMode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringTestEnvironment {

  public Thumb getHolderThumb() {
    LOG.info("Auth URI: {}", System.getProperty("register-auth-uri"));
    LOG.info("Resource URI: {}", System.getProperty("register-resource-uri"));

    URI authUri = URI.create(System.getProperty("register-auth-uri"));
    URI resourceUri = URI.create(System.getProperty("register-resource-uri"));

    return new Thumb(ThumbConfig.builder()
        .register(ThumbConfigRegister.builder().resourceUri(resourceUri)
            .auth(ThumbConfigAuth.builder().issuerUri(authUri)
                .authMethod(ThumbAuthMethod.CLIENT_CREDENTIALS)
                .clientSecret(TestDataConstants.HOLDER_CLIENT_SECRET)
                .clientId(TestDataConstants.HOLDER_CLIENT_ID).build())
            .build())
        .build());
  }
}
