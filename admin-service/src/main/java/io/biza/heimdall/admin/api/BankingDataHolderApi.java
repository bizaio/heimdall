package io.biza.heimdall.admin.api;

import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderApiDelegate;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = Constants.TAG_DATA_HOLDER_NAME, description = Constants.TAG_DATA_HOLDER_DESCRIPTION)
@RequestMapping("/v1/data-holder")
public interface BankingDataHolderApi {

  default BankingDataHolderApiDelegate getDelegate() {
    return new BankingDataHolderApiDelegate() {};
  }

  @Operation(summary = "List all Holders", description = "List all Holders",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioDataHolder.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_READ)
  default ResponseEntity<List<DioDataHolder>> listHolders() {
    return getDelegate().listHolders();
  }
  
  @Operation(summary = "Get a single Holder", description = "Returns a single holder entry",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioDataHolder.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_NOT_FOUND,
          description = Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{holderId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_READ)
  default ResponseEntity<DioDataHolder> getHolder(
      @NotNull @Valid @PathVariable("holderId") UUID holderId) {
    return getDelegate().getHolder(holderId);
  }

  @Operation(summary = "Create a Holder", description = "Creates and Returns a new Holder",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_CREATED,
          description = Constants.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioDataHolder.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Constants.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<DioDataHolder> createHolder(
      @NotNull @RequestBody DioDataHolder holder) throws ValidationListException {
    return getDelegate().createHolder(holder);
  }

  @Operation(summary = "Update a single Holder",
      description = "Updates and Returns an existing Holder",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioDataHolder.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Constants.RESPONSE_INPUT_VALIDATION_ERROR, content = @Content(
              array = @ArraySchema(schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{holderId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<DioDataHolder> updateHolder(
      @NotNull @Valid @PathVariable("holderId") UUID holderId,
      @NotNull @RequestBody DioDataHolder holder) throws ValidationListException {
    return getDelegate().updateHolder(holderId, holder);
  }

  @Operation(summary = "Delete a single Holder", description = "Deletes a Holder",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioDataHolder.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_NOT_FOUND,
          description = Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{holderId}")
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<Void> deleteHolder(
      @NotNull @Valid @PathVariable("holderId") UUID holderId) {
    return getDelegate().deleteHolder(holderId);
  }

}

