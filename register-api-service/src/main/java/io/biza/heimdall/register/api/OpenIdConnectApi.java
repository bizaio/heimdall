package io.biza.heimdall.register.api;

import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.OpenIdConnectApiDelegate;
import io.biza.heimdall.register.exceptions.CryptoException;
import io.biza.heimdall.register.exceptions.InvalidClientException;
import io.biza.heimdall.register.exceptions.InvalidRequestException;
import io.biza.heimdall.register.exceptions.InvalidScopeException;
import io.biza.heimdall.register.exceptions.NotInitialisedException;
import io.biza.heimdall.register.exceptions.UnsupportedGrantTypeException;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;
import io.biza.thumb.oidc.payloads.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Tag(name = Constants.TAG_BANKING_DATA_RECIPIENT_NAME,
    description = Constants.TAG_BANKING_DATA_RECIPIENT_DESCRIPTION)
@RequestMapping("/")
public interface OpenIdConnectApi {

  default OpenIdConnectApiDelegate getDelegate() {
    return new OpenIdConnectApiDelegate() {};
  }

  @Operation(summary = "Get Discovery Document",
      description = "Get an OpenID Configuration Document")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns the openid-configuration document",
      content = @Content(schema = @Schema(implementation = ProviderDiscoveryMetadata.class)))})
  @RequestMapping(path = "/.well-known/openid-configuration", method = RequestMethod.GET)
  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return getDelegate().getDiscoveryDocument();
  }

  @Operation(summary = "Perform a Token Login", description = "Get an OAuth2 Token")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a Bearer token",
      content = @Content(schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/oauth2/token", method = RequestMethod.POST)
  default ResponseEntity<TokenResponse> tokenLogin(
      @NotNull @RequestBody ObjectNode tokenLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    return getDelegate().tokenLogin(tokenLoginRequest);
  }

}

