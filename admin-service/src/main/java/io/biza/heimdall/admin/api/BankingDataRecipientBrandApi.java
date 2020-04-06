package io.biza.heimdall.admin.api;

import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandApiDelegate;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipientBrand;
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

@Tag(name = Constants.TAG_DATA_RECIPIENT_NAME,
    description = Constants.TAG_DATA_RECIPIENT_DESCRIPTION)
@RequestMapping("/v1/data-recipient/{recipientId}/brand")
public interface BankingDataRecipientBrandApi {

  default BankingDataRecipientBrandApiDelegate getDelegate() {
    return new BankingDataRecipientBrandApiDelegate() {};
  }

  @Operation(summary = "List all Recipient Brands", description = "List all Recipient Brands",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
      description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioDataRecipientBrand.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<List<DioDataRecipientBrand>> listRecipientBrandes(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId) {
    return getDelegate().listRecipientBrands(recipientId);
  }

  @Operation(summary = "Get a single Recipient Brand",
      description = "Returns a single recipient brand entry",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioDataRecipientBrand.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{brandId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<DioDataRecipientBrand> getRecipientBrand(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId) throws NotFoundException {
    return getDelegate().getRecipientBrand(recipientId, brandId);
  }

  @Operation(summary = "Create a Recipient Brand",
      description = "Creates and Returns a new Recipient Brand",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_CREATED,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioDataRecipientBrand.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioDataRecipientBrand> createRecipientBrand(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @RequestBody DioDataRecipientBrand brand) throws ValidationListException, NotFoundException {
    return getDelegate().createRecipientBrand(recipientId, brand);
  }

  @Operation(summary = "Update a single Recipient Brand",
      description = "Updates and Returns an existing Recipient Brand",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioDataRecipientBrand.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{brandId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioDataRecipientBrand> updateRecipientBrand(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @RequestBody DioDataRecipientBrand recipient) throws ValidationListException, NotFoundException {
    return getDelegate().updateRecipientBrand(recipientId, brandId, recipient);
  }

  @Operation(summary = "Delete a single RecipientBrand", description = "Deletes a RecipientBrand",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioDataRecipientBrand.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{brandId}")
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<Void> deleteRecipientBrand(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId) throws NotFoundException {
    return getDelegate().deleteRecipientBrand(recipientId, brandId);
  }

}

