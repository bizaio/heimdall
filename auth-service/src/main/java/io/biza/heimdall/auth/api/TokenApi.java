package io.biza.heimdall.auth.api;

import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.api.delegate.TokenApiDelegate;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
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

@Tag(name = Constants.TAG_TOKEN_NAME,
    description = Constants.TAG_TOKEN_DESCRIPTION)
public interface TokenApi {

  default TokenApiDelegate getDelegate() {
    return new TokenApiDelegate() {};
  }

  @Operation(summary = "Perform a Token Login", description = "Get an OAuth2 Token")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a Bearer token",
      content = @Content(schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/token", method = RequestMethod.POST)
  default ResponseEntity<TokenResponse> tokenLogin(
      @NotNull @RequestBody ObjectNode tokenLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    return getDelegate().tokenLogin(tokenLoginRequest);
  }

}

