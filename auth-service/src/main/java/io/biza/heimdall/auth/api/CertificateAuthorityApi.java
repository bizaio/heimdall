package io.biza.heimdall.auth.api;

import io.biza.heimdall.auth.Constants;
import io.biza.heimdall.auth.api.delegate.CertificateAuthorityApiDelegate;
import io.biza.heimdall.auth.api.delegate.DiscoveryApiDelegate;
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

@Tag(name = Constants.TAG_CA_NAME,
    description = Constants.TAG_CA_DESCRIPTION)
public interface CertificateAuthorityApi {

  default CertificateAuthorityApiDelegate getDelegate() {
    return new CertificateAuthorityApiDelegate() {};
  }

  @Operation(summary = "Get Register Certificate Authority",
      description = "Get Register Certificate Authority")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns the public key of the Register Certificate Authority",
      content = @Content(schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/ca", method = RequestMethod.GET)
  default ResponseEntity<String> getCertificateAuthority() {
    return getDelegate().getCertificateAuthority();
  }

}

