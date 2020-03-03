package io.biza.heimdall.auth.api;

import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.api.delegate.TokenApiDelegate;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
import io.biza.heimdall.auth.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Tag(name = Constants.TAG_TOKEN_NAME, description = Constants.TAG_TOKEN_DESCRIPTION)
@RequestMapping("/oidc")
public interface TokenApi {

  default TokenApiDelegate getDelegate() {
    return new TokenApiDelegate() {};
  }

  @Operation(summary = "Retrieve a Token",
      description = "Retrieve a Token using an OAuth2 Token Request")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a Bearer token",
      content = @Content(schema = @Schema(implementation = TokenResponse.class)))})
  @RequestMapping(path = "/token", method = RequestMethod.POST,
      consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE },
      produces = { MediaType.APPLICATION_JSON_VALUE })
  default ResponseEntity<TokenResponse> tokenLogin(
      @RequestParam Map<String,String> rawLoginRequest)
      throws InvalidRequestException, InvalidClientException, InvalidScopeException,
      UnsupportedGrantTypeException, NotInitialisedException, CryptoException {
    return getDelegate().tokenLogin(rawLoginRequest);
  }

}

