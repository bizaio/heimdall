package io.biza.heimdall.register.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import io.biza.babelfish.oidc.payloads.JWKS;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.interfaces.IssuerService;
import io.biza.heimdall.register.api.delegate.DiscoveryApiDelegate;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class DiscoveryApiDelegateImpl implements DiscoveryApiDelegate {

	@Value("${heimdall.issuerId:dio-register}")
	String issuerId;

	@Autowired
	IssuerService issuerService;

	@Override
	public ResponseEntity<JWKS> getJwks() throws NotInitialisedException {
		return ResponseEntity.ok(issuerService.jwks(issuerId));
	}
}
