package io.biza.heimdall.register.api;

import io.biza.heimdall.payload.registration.SoftwareStatementAssertion;
import io.biza.heimdall.payload.responses.DataRecipientsStatusList;
import io.biza.heimdall.payload.responses.ResponseRegisterDataRecipientList;
import io.biza.heimdall.payload.responses.SoftwareProductsStatusList;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.heimdall.register.api.delegate.RegisterApiDelegate;
import io.biza.heimdall.shared.util.RawJson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = Constants.TAG_BANKING_DATA_RECIPIENT_NAME,
    description = Constants.TAG_BANKING_DATA_RECIPIENT_DESCRIPTION)
@RequestMapping("/v1")
public interface RegisterApi {

  default RegisterApiDelegate getDelegate() {
    return new RegisterApiDelegate() {};
  }

  @Operation(summary = "Get Register JWKS", description = "Get Register JWKS")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a JWKS containing all Active Register Public Java Web Keys in JWKS format", content = @Content(
          schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/jwks", method = RequestMethod.GET)
  default ResponseEntity<String> getJwks() {
    return getDelegate().getJwks();
  }
  
  @Operation(summary = "Get Register Certificate Authoirty", description = "Get Register Certificate Authority")
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns the public key of the Register Certificate Authority", content = @Content(
          schema = @Schema(implementation = String.class)))})
  @RequestMapping(path = "/ca", method = RequestMethod.GET)
  default ResponseEntity<String> getCertificateAuthority() {
    return getDelegate().getCertificateAuthority();
  }

}

