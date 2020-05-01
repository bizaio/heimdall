package io.biza.heimdall.register.api;

import io.biza.babelfish.cdr.enumerations.register.IndustryType;
import io.biza.babelfish.cdr.models.requests.register.RequestGetDataHolderBrandsV1;
import io.biza.babelfish.cdr.models.responses.register.ResponseRegisterDataHolderBrandListV1;
import io.biza.heimdall.register.Constants;
import io.biza.heimdall.register.api.delegate.DataHolderApiDelegate;
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
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = Constants.TAG_BANKING_DATA_HOLDER_NAME, description = Constants.TAG_BANKING_DATA_HOLDER_DESCRIPTION)
@RequestMapping("/v1/{industry}/data-holders")
public interface DataHolderApi {

	default DataHolderApiDelegate getDelegate() {
		return new DataHolderApiDelegate() {
		};
	}

	@Operation(summary = "Get Holder Brands", description = "Get Data Holder Brands from the Register", parameters = {
			@Parameter(name = "x-v", in = ParameterIn.HEADER, description = "Version of the API end point requested by the client. Must be set to a positive integer.") }, security = {
					@SecurityRequirement(name = "cdr-register", scopes = Constants.SECURITY_SCOPE_REGISTER_BANK_READ) })
	@ApiResponses(value = {
			@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK, description = "Returns a Response containing the Data Holder Brands", content = @Content(schema = @Schema(implementation = ResponseRegisterDataHolderBrandListV1.class))) })
	@RequestMapping(path = "/brands", method = RequestMethod.GET)
	@PreAuthorize(Constants.OAUTH2_SCOPE_REGISTER_BANK_READ)
	default ResponseEntity<ResponseRegisterDataHolderBrandListV1> getBankingDataHolderBrands(
			@Valid @NotNull @PathVariable(name = "industry", required = true) IndustryType industry,
			@Valid @RequestParam(name = "updated-since", required = false) OffsetDateTime updatedSince,
			@Valid @RequestParam(name = "page", required = false, defaultValue = "1") @Min(1) Integer page,
			@Valid @RequestParam(name = "page-size", required = false, defaultValue = "25") Integer pageSize) {
		return getDelegate().getBankingDataHolderBrands(
				RequestGetDataHolderBrandsV1.builder().industryType(industry).updatedSince(updatedSince).build(),
				PageRequest.of(page != null ? page - 1 : 0, pageSize != null ? pageSize : 25));
	}

}
