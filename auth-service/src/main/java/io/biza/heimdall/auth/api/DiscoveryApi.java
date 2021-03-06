package io.biza.heimdall.auth.api;

import io.biza.babelfish.oidc.payloads.TokenResponse;
import io.biza.babelfish.oidc.requests.ProviderDiscoveryMetadata;
import io.biza.heimdall.auth.api.delegate.DiscoveryApiDelegate;
import io.biza.heimdall.auth.api.delegate.TokenApiDelegate;
import io.biza.heimdall.auth.exceptions.CryptoException;
import io.biza.heimdall.auth.exceptions.InvalidClientException;
import io.biza.heimdall.auth.exceptions.InvalidRequestException;
import io.biza.heimdall.auth.exceptions.InvalidScopeException;
import io.biza.heimdall.auth.exceptions.NotInitialisedException;
import io.biza.heimdall.auth.exceptions.UnsupportedGrantTypeException;
import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.shared.util.RawJson;
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

@Tag(name = Constants.TAG_DISCOVERY_NAME,
    description = Constants.TAG_DISCOVERY_DESCRIPTION)
@RequestMapping("/")
public interface DiscoveryApi {

  default DiscoveryApiDelegate getDelegate() {
    return new DiscoveryApiDelegate() {};
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
  
  @Operation(summary = "Get Register JWKS", description = "Get Register JWKS")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a JWKS containing all Active Register Public Java Web Keys in JWKS format",
      content = @Content(schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/jwks", method = RequestMethod.GET)
  default ResponseEntity<RawJson> getJwks() {
    return getDelegate().getJwks();
  }

}

