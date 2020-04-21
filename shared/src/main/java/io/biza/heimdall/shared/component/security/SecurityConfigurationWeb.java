package io.biza.heimdall.shared.component.security;

import java.security.Provider;
import java.security.Security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;

@Configuration
@EnableWebSecurity
public class SecurityConfigurationWeb extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		/**
		 * Inject RSASSA-PSS support into runtime
		 */
		Provider bc = BouncyCastleProviderSingleton.getInstance();
		Security.addProvider(bc);

		/**
		 * Validate JWT
		 */
		http.oauth2ResourceServer().jwt();
	}
}
