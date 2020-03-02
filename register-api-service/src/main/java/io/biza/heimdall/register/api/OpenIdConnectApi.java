package io.biza.heimdall.register.api;

import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.OpenIdConnectApiDelegate;
import io.biza.thumb.oidc.payloads.ProviderDiscoveryMetadata;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = Constants.TAG_BANKING_DATA_RECIPIENT_NAME,
    description = Constants.TAG_BANKING_DATA_RECIPIENT_DESCRIPTION)
@RequestMapping("/")
public interface OpenIdConnectApi {

  default OpenIdConnectApiDelegate getDelegate() {
    return new OpenIdConnectApiDelegate() {};
  }

  @Operation(summary = "Get Discovery Document", description = "Get an OpenID Configuration Document")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns the openid-configuration document", content = @Content(
          schema = @Schema(implementation = ProviderDiscoveryMetadata.class)))})
  @RequestMapping(path = "/.well-known/openid-configuration", method = RequestMethod.GET)
  default ResponseEntity<ProviderDiscoveryMetadata> getDiscoveryDocument() {
    return getDelegate().getDiscoveryDocument();
  }
}

