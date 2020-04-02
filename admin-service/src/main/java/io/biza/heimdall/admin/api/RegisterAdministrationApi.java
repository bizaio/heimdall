package io.biza.heimdall.admin.api;

import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.RegisterAdministrationApiDelegate;
import io.biza.heimdall.shared.payloads.requests.dio.RequestCACertificateSign;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = Constants.TAG_DATA_REGISTER_ADMIN_NAME,
    description = Constants.TAG_DATA_REGISTER_ADMIN_DESCRIPTION)
@RequestMapping("/v1/register")
public interface RegisterAdministrationApi {

  default RegisterAdministrationApiDelegate getDelegate() {
    return new RegisterAdministrationApiDelegate() {};
  }

  @Operation(summary = "Sign a certificate signing request using the Heimdall CA Certificate",
      description = "This accepts a CSR and signs it using the Certificate Authority generated on startup by Heimdall",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.OAUTH2_SCOPE_KEY_ADMIN})})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = String.class))))})
  @RequestMapping(path = "/ca/sign", method = RequestMethod.POST)
  @PreAuthorize(Constants.OAUTH2_SCOPE_KEY_ADMIN)
  default ResponseEntity<String> signCertificate(
      @NotNull @RequestBody RequestCACertificateSign createRequest) {
    return getDelegate().signCertificate(createRequest);
  }
}

