package io.biza.heimdall.admin.api;

import io.biza.babelfish.cdr.exceptions.NotFoundException;
import io.biza.babelfish.cdr.exceptions.ValidationListException;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientBrandSoftwareProductApiDelegate;
import io.biza.heimdall.shared.payloads.dio.DioSoftwareProduct;
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
@RequestMapping("/v1/data-recipient/{recipientId}/brand/{brandId}/software-product")
public interface BankingDataRecipientBrandSoftwareProductApi {

  default BankingDataRecipientBrandSoftwareProductApiDelegate getDelegate() {
    return new BankingDataRecipientBrandSoftwareProductApiDelegate() {};
  }

  @Operation(summary = "List all Recipient Brand Software Products",
      description = "List all Recipient Brand Software Products",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
      description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioSoftwareProduct.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<List<DioSoftwareProduct>> listRecipientBrandSoftwareProducts(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId) {
    return getDelegate().listRecipientBrandSoftwareProducts(recipientId, brandId);
  }

  @Operation(summary = "Get a single Recipient Software Product",
      description = "Returns a single recipient Software Product entry",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioSoftwareProduct.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{softwareProductId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<DioSoftwareProduct> getRecipientSoftwareProduct(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("softwareProductId") UUID softwareProductId) throws NotFoundException {
    return getDelegate().getRecipientBrandSoftwareProduct(recipientId, brandId, softwareProductId);
  }

  @Operation(summary = "Create a Recipient Software Product",
      description = "Creates and Returns a new Recipient Software Product",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_CREATED,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioSoftwareProduct.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioSoftwareProduct> createRecipientSoftwareProduct(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @RequestBody DioSoftwareProduct softwareProduct) throws ValidationListException, NotFoundException {
    return getDelegate().createRecipientBrandSoftwareProduct(recipientId, brandId, softwareProduct);
  }

  @Operation(summary = "Update a single Recipient Software Product",
      description = "Updates and Returns an existing Recipient Software Product",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioSoftwareProduct.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{softwareProductId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioSoftwareProduct> updateRecipientSoftwareProduct(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("softwareProductId") UUID softwareProductId,
      @NotNull @RequestBody DioSoftwareProduct recipient) throws ValidationListException, NotFoundException {
    return getDelegate().updateRecipientBrandSoftwareProduct(recipientId, brandId,
        softwareProductId, recipient);
  }

  @Operation(summary = "Delete a single Recipient Software Product",
      description = "Deletes a Recipient Software Product",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioSoftwareProduct.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{softwareProductId}")
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<Void> deleteRecipientSoftwareProduct(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @Valid @PathVariable("brandId") UUID brandId,
      @NotNull @Valid @PathVariable("softwareProductId") UUID softwareProductId) throws NotFoundException {
    return getDelegate().deleteRecipientBrandSoftwareProduct(recipientId, brandId,
        softwareProductId);
  }

}

