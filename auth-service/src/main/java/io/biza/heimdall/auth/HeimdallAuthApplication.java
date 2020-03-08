package io.biza.heimdall.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import io.biza.heimdall.auth.Constants;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan({"io.biza.heimdall.shared.component.mapper",
    "io.biza.heimdall.shared.component.persistence", "io.biza.heimdall.shared.loaders",
    "io.biza.heimdall.auth", "io.biza.babelfish.spring"})
@Slf4j
public class HeimdallAuthApplication {

  public static void main(String[] args) {
    SpringApplicationBuilder application =
        new SpringApplicationBuilder(HeimdallAuthApplication.class);
    application.run(args);

  }

  @Bean
  public OpenAPI customOpenAPI(@Value("${heimdall.version}") String appVersion) {
    /**
     * OpenID Connect is available in OAS annotations but not yet in swagger-ui :(
     * https://github.com/swagger-api/swagger-ui/issues/3517
     * 
     * TODO: Contribute OpenID support to swagger-ui We want our swagger definition to be accurate
     * so we leave this as is but it means swagger-ui is a "view spec" only interface
     */
    return new OpenAPI()
        .components(new Components().addSecuritySchemes(Constants.SECURITY_SCHEME_NAME,
            new SecurityScheme().type(SecurityScheme.Type.OPENIDCONNECT)
                .openIdConnectUrl(Constants.OPENID_CONNECT_URL)))
        .info(new Info().title("Heimdall Auth Server").version(appVersion).description(
            "This is the Heimdall Auth Server. You can find out more about Heimdall at [https://github.com/bizaio/heimdall](https://github.com/bizaio/heimdall) or on the [DataRight.io Slack, #oss](https://join.slack.com/t/datarightio/shared_invite/enQtNzAyNTI2MjA2MzU1LTU1NGE4MmQ2N2JiZWI2ODA5MTQ2N2Q0NTRmYmM0OWRlM2U5YzA3NzU5NDYyODlhNjRmNzU3ZDZmNTI0MDE3NjY).")
            .license(new License().name("GPL 3.0")
                .url("https://github.com/bizaio/heimdall/blob/develop/LICENSE")));
  }

}
