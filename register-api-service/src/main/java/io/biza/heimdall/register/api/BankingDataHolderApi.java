package io.biza.heimdall.register.api;

import io.biza.heimdall.payload.responses.RequestGetDataHolderBrands;
import io.biza.heimdall.payload.responses.ResponseRegisterDataHolderBrandList;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.BankingDataHolderApiDelegate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = Constants.TAG_BANKING_DATA_HOLDER_NAME, description = Constants.TAG_BANKING_DATA_HOLDER_DESCRIPTION)
@RequestMapping("/v1/banking/data-holders")
public interface BankingDataHolderApi {

  default BankingDataHolderApiDelegate getDelegate() {
    return new BankingDataHolderApiDelegate() {};
  }

  @Operation(summary = "Get Holder Brands",
      description = "Get Data Holder Brands from the Register", parameters = {
          @Parameter(name = "x-v", in = ParameterIn.HEADER,
              description = "Version of the API end point requested by the client. Must be set to a positive integer.")
  },
  security = {@SecurityRequirement(name = "cdr-register",
      scopes = Constants.SECURITY_SCOPE_HOLDER_READ)})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = "Returns a Response containing the Data Holder Brands",
      content = @Content(schema = @Schema(implementation = ResponseRegisterDataHolderBrandList.class)))})
  @RequestMapping(path = "/brands", method = RequestMethod.GET)
  default ResponseEntity<ResponseRegisterDataHolderBrandList> getBankingDataHolderBrands(
      @Valid @RequestParam(name = "updated-since",
      required = false) OffsetDateTime updatedSince,
  @Valid @RequestParam(name = "page", required = false,
      defaultValue = "1") @Min(1) Integer page,
  @Valid @RequestParam(name = "page-size", required = false,
      defaultValue = "25") Integer pageSize ) {
    return getDelegate().getBankingDataHolderBrands(RequestGetDataHolderBrands.builder().updatedSince(updatedSince).page(page).pageSize(pageSize).build());
  }

}

