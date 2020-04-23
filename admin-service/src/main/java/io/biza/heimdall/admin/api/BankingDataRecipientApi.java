package io.biza.heimdall.admin.api;

import io.biza.babelfish.spring.exceptions.ValidationListException;
import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataRecipientApiDelegate;
import io.biza.babelfish.spring.exceptions.NotFoundException;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
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
@RequestMapping("/v1/data-recipient")
public interface BankingDataRecipientApi {

  default BankingDataRecipientApiDelegate getDelegate() {
    return new BankingDataRecipientApiDelegate() {};
  }

  @Operation(summary = "List all Recipients", description = "List all Recipients",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
      description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioDataRecipient.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<List<DioDataRecipient>> listRecipients() {
    return getDelegate().listRecipients();
  }

  @Operation(summary = "Get a single Recipient", description = "Returns a single recipient entry",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioDataRecipient.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{recipientId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_READ)
  default ResponseEntity<DioDataRecipient> getRecipient(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId) throws NotFoundException {
    return getDelegate().getRecipient(recipientId);
  }

  @Operation(summary = "Create a Recipient", description = "Creates and Returns a new Recipient",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_CREATED,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioDataRecipient.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioDataRecipient> createRecipient(
      @NotNull @RequestBody DioDataRecipient recipient) throws ValidationListException {
    return getDelegate().createRecipient(recipient);
  }

  @Operation(summary = "Update a single Recipient",
      description = "Updates and Returns an existing Recipient",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioDataRecipient.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = io.biza.babelfish.spring.Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{recipientId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<DioDataRecipient> updateRecipient(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId,
      @NotNull @RequestBody DioDataRecipient recipient) throws ValidationListException, NotFoundException {
    return getDelegate().updateRecipient(recipientId, recipient);
  }

  @Operation(summary = "Delete a single Recipient", description = "Deletes a Recipient",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_RECIPIENT_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_OK,
          description = io.biza.babelfish.spring.Constants.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioDataRecipient.class))),
      @ApiResponse(responseCode = io.biza.babelfish.spring.Constants.RESPONSE_CODE_NOT_FOUND,
          description = io.biza.babelfish.spring.Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{recipientId}")
  @PreAuthorize(Constants.OAUTH2_SCOPE_RECIPIENT_WRITE)
  default ResponseEntity<Void> deleteRecipient(
      @NotNull @Valid @PathVariable("recipientId") UUID recipientId) throws NotFoundException {
    return getDelegate().deleteRecipient(recipientId);
  }

}

