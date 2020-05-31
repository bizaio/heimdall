package io.biza.heimdall.register.api;

import io.biza.babelfish.common.exceptions.NotInitialisedException;
import io.biza.babelfish.oidc.payloads.JWKS;
import io.biza.heimdall.register.api.delegate.DiscoveryApiDelegate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import io.biza.heimdall.register.Constants;

@Tag(name = Constants.TAG_BANKING_REGISTER_NAME,
    description = Constants.TAG_BANKING_REGISTER_DESCRIPTION)
@RequestMapping("/")
public interface DiscoveryApi {

  default DiscoveryApiDelegate getDelegate() {
    return new DiscoveryApiDelegate() {};
  }
  
  @Operation(summary = "Get Register JWKS", description = "Get Register JWKS")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a JWKS containing all Active Register Public Java Web Keys in JWKS format",
      content = @Content(schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/jwks", method = RequestMethod.GET)
  default ResponseEntity<JWKS> getJwks() throws NotInitialisedException {
    return getDelegate().getJwks();
  }

}

