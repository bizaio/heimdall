package io.biza.heimdall.register.api;

import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.common.exceptions.NotFoundException;
import io.biza.babelfish.common.exceptions.NotInitialisedException;
import io.biza.babelfish.common.exceptions.SigningOperationException;
import io.biza.babelfish.cdr.models.payloads.register.SoftwareStatementAssertionV1;
import io.biza.babelfish.cdr.models.responses.register.DataRecipientsStatusListV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataRecipientListV1;
import io.biza.babelfish.cdr.models.responses.register.SoftwareProductsStatusListV1;
import io.biza.babelfish.cdr.support.RawJson;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.DataRecipientApiDelegate;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = Constants.TAG_BANKING_DATA_RECIPIENT_NAME,
    description = Constants.TAG_BANKING_DATA_RECIPIENT_DESCRIPTION)
@RequestMapping("/v1/{industry}/data-recipients")
public interface DataRecipientApi {

  default DataRecipientApiDelegate getDelegate() {
    return new DataRecipientApiDelegate() {};
  }

  @Operation(summary = "Get Data Recipients", description = "Get Data Recipients from the Register",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.")})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a Response containing the Data Recipients", content = @Content(
          schema = @Schema(implementation = ResponseRegisterDataRecipientListV1.class)))})
  @RequestMapping(method = RequestMethod.GET)
  default ResponseEntity<ResponseRegisterDataRecipientListV1> getBankingDataRecipients(@Valid @NotNull @PathVariable(name = "industry", required = true) IndustryType industry) {
    return getDelegate().getBankingDataRecipients(industry);
  }

  @Operation(summary = "Get Data Recipient Statuses",
      description = "Get a list of Data Recipient Statuses",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.")})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a response containing a set of Data Recipient statuses",
      content = @Content(schema = @Schema(implementation = DataRecipientsStatusListV1.class)))})
  @RequestMapping(path = "/status", method = RequestMethod.GET)
  default ResponseEntity<DataRecipientsStatusListV1> getBankingDataRecipientStatuses(@Valid @NotNull @PathVariable(name = "industry", required = true) IndustryType industry) {
    return getDelegate().getBankingDataRecipientStatuses(industry);
  }

  @Operation(
      summary = "Get a Software Statement Assertion for a Software Product on the CDR Register",
      description = "Get a list of Data Recipient Statuses",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.")},
      security = {@SecurityRequirement(name = "cdr-register",
          scopes = Constants.SECURITY_SCOPE_REGISTER_BANK_READ)})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Get a Software Statement Assertion (SSA) for a software product on the CDR Register to be used for Dynamic Registration with a Data Holder",
      content = @Content(schema = @Schema(implementation = SoftwareStatementAssertionV1.class)))})
  @RequestMapping(path = "/brands/{brandId}/software-products/{productId}/ssa",
      method = RequestMethod.GET)
  @PreAuthorize(Constants.OAUTH2_SCOPE_REGISTER_BANK_READ)
  default ResponseEntity<RawJson> getSoftwareStatementAssertion(
		  @Valid @NotNull @PathVariable(name = "industry", required = true) IndustryType industry,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("productId") UUID productId) throws SigningOperationException, NotFoundException, NotInitialisedException {
    return getDelegate().getSoftwareStatementAssertion(industry, brandId, productId);
  }


  @Operation(summary = "Get the status for software products",
      description = "Get the statuses for software products from the CDR Register",
      parameters = {@Parameter(name = "x-v", in = ParameterIn.HEADER,
          description = "Version of the API end point requested by the client. Must be set to a positive integer.")})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a list of software products from the Register",
      content = @Content(schema = @Schema(implementation = SoftwareProductsStatusListV1.class)))})
  @RequestMapping(path = "/brands/software-products/status", method = RequestMethod.GET)
  default ResponseEntity<SoftwareProductsStatusListV1> getSoftwareProductStatuses(@Valid @NotNull @PathVariable(name = "industry", required = true) IndustryType industry) {
    return getDelegate().getSoftwareProductStatuses(industry);
  }

}

