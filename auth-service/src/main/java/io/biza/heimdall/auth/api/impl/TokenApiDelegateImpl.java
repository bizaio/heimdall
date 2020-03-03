package io.biza.heimdall.auth.api.impl;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.biza.babelfish.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.babelfish.oidc.enumerations.OAuth2ClientAssertionType;
import io.biza.babelfish.oidc.enumerations.OAuth2GrantType;
import io.biza.babelfish.oidc.enumerations.OAuth2ResponseType;
import io.biza.babelfish.oidc.enumerations.OIDCAuthMethod;
import io.biza.babelfish.oidc.enumerations.OIDCGrantType;
import io.biza.babelfish.oidc.enumerations.OIDCSubjectType;
import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.babelfish.oidc.requests.RequestTokenClientCredentials;
import io.biza.babelfish.oidc.requests.RequestTokenPrivateKeyJwt;
import io.biza.heimdall.auth.api.delegate.TokenApiDelegate;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
import io.biza.heimdall.auth.service.ClientCredentialService;
import io.biza.heimdall.auth.service.PrivateKeyJwtService;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.util.EndpointUtil;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class TokenApiDelegateImpl implements TokenApiDelegate {

  @Value("${heimdall.hostname}")
  private String hostname;

  @Autowired
  ClientCredentialService clientCredentialService;

  @Autowired
  PrivateKeyJwtService jwtCredentialService;

  @Autowired
  private ObjectMapper jackson;

  @Override
  public ResponseEntity<TokenResponse> tokenLogin(Map<String, String> tokenLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    if (tokenLoginRequest.containsKey("grant_type")) {
      if (tokenLoginRequest.get("grant_type")
          .equals(OAuth2GrantType.CLIENT_CREDENTIALS.toString())) {

        if (tokenLoginRequest.containsKey("client_assertion_type")) {
          if (tokenLoginRequest.get("client_assertion_type")
              .equals(OAuth2ClientAssertionType.JWT_BEARER.toString())) {
            try {
              return jwtCredentialService.tokenLogin(jackson.convertValue(tokenLoginRequest, RequestTokenPrivateKeyJwt.class));
            } catch (IllegalArgumentException e) {
              LOG.warn("Illegal Argument Encountered for JWT Bearer", e.getCause());
              throw new InvalidRequestException();
            }
          } else {
            throw new InvalidRequestException();
          }

        } else {
          try {
            return clientCredentialService.tokenLogin(
                jackson.convertValue(tokenLoginRequest, RequestTokenClientCredentials.class));
          } catch (IllegalArgumentException e) {
            LOG.warn("Illegal Argument Encountered for Client Credentials", e.getCause());
            throw new InvalidRequestException();
          }
        }
      } else {
        LOG.warn("Unsupported grant type of {} requested, rejecting", tokenLoginRequest.get("grant_type"));
        throw new UnsupportedGrantTypeException();
      }
    } else {
      LOG.error("Request does not contain a grant_type value, rejecting");
      throw new InvalidRequestException();
    }
  }
}
