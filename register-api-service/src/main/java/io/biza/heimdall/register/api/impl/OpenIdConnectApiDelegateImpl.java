package io.biza.heimdall.register.api.impl;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.OpenIdConnectApiDelegate;
import io.biza.heimdall.register.exceptions.CryptoException;
import io.biza.heimdall.register.exceptions.InvalidClientException;
import io.biza.heimdall.register.exceptions.InvalidRequestException;
import io.biza.heimdall.register.exceptions.InvalidScopeException;
import io.biza.heimdall.register.exceptions.NotInitialisedException;
import io.biza.heimdall.register.exceptions.UnsupportedGrantTypeException;
import io.biza.heimdall.register.service.ClientCredentialService;
import io.biza.heimdall.register.service.PrivateKeyJwtService;
import io.biza.heimdall.register.util.EndpointUtil;
import io.biza.thumb.oidc.enumerations.JWSSigningAlgorithmType;
import io.biza.thumb.oidc.enumerations.OAuth2ClientAssertionType;
import io.biza.thumb.oidc.enumerations.OAuth2GrantType;
import io.biza.thumb.oidc.enumerations.OAuth2ResponseType;
import io.biza.thumb.oidc.enumerations.OIDCAuthMethod;
import io.biza.thumb.oidc.enumerations.OIDCGrantType;
import io.biza.thumb.oidc.enumerations.OIDCSubjectType;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;
import io.biza.thumb.oidc.payloads.TokenResponse;
import io.biza.thumb.oidc.requests.RequestTokenClientCredentials;
import io.biza.thumb.oidc.requests.RequestTokenPrivateKeyJwt;
import lombok.extern.slf4j.Slf4j;

@Validated
@Controller
@Slf4j
public class OpenIdConnectApiDelegateImpl implements OpenIdConnectApiDelegate {

  @Value("${heimdall.hostname}")
  private String hostname;

  @Autowired
  ClientCredentialService clientCredentialService;

  @Autowired
  PrivateKeyJwtService jwtCredentialService;

  @Autowired
  private ObjectMapper jackson;

  @Override
  public ResponseEntity<TokenResponse> tokenLogin(@NotNull ObjectNode tokenLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    if (tokenLoginRequest.has("grant_type")) {
      if (tokenLoginRequest.get("grant_type").asText()
          .equals(OAuth2GrantType.CLIENT_CREDENTIALS.toString())) {

        if (tokenLoginRequest.has("client_assertion_type")) {
          if (tokenLoginRequest.get("client_assertion_type").asText()
              .equals(OAuth2ClientAssertionType.JWT_BEARER.toString())) {
            try {
              return jwtCredentialService.tokenLogin(
                  jackson.treeToValue(tokenLoginRequest, RequestTokenPrivateKeyJwt.class));
            } catch (JsonProcessingException e) {
              throw new InvalidRequestException();
            }
          } else {
            throw new InvalidRequestException();
          }

        } else {
          try {
            return clientCredentialService.tokenLogin(
                jackson.treeToValue(tokenLoginRequest, RequestTokenClientCredentials.class));
          } catch (JsonProcessingException e) {
            throw new InvalidRequestException();
          }
        }
      } else {
        throw new UnsupportedGrantTypeException();
      }
    } else {
      throw new InvalidRequestException();
    }
  }

  @Override
  public ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {


    return ResponseEntity.ok(ProviderDiscoveryMetadata.builder().issuer(EndpointUtil.issuerUri())
        .jwksUri(EndpointUtil.jwksUri()).tokenEndpoint(EndpointUtil.tokenEndpoint())
        .claimsSupported(List.of("sub"))
        .idTokenSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .subjectTypesSupported(List.of(OIDCSubjectType.PUBLIC))
        .scopesSupported(Constants.REGISTER_SCOPE_LIST)
        .responseTypesSupported(List.of(List.of(OAuth2ResponseType.TOKEN)))
        .grantTypesSupported(List.of(OIDCGrantType.CLIENT_CREDENTIALS))
        .tokenEndpointAuthMethods(List.of(OIDCAuthMethod.PRIVATE_KEY_JWT)).oauthMtlsSupported(true)
        .requestObjectSigningAlgorithms(List.of(JWSSigningAlgorithmType.PS256))
        .requestUriSupport(null).build());
  }

}
