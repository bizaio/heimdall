package io.biza.heimdall.admin.api;

import io.biza.heimdall.admin.Constants;
import io.biza.heimdall.admin.api.delegate.BankingDataHolderClientApiDelegate;
import io.biza.heimdall.shared.exceptions.NotFoundException;
import io.biza.heimdall.shared.exceptions.ValidationListException;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderClient;
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
@RequestMapping("/v1/data-holder/{holderId}/client")
public interface BankingDataHolderClientApi {

  default BankingDataHolderClientApiDelegate getDelegate() {
    return new BankingDataHolderClientApiDelegate() {};
  }

  @Operation(summary = "List all Holder Clients", description = "List all Holder Clients",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_READ})})
  @ApiResponses(value = {@ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
      description = Constants.RESPONSE_SUCCESSFUL_LIST, content = @Content(
          array = @ArraySchema(schema = @Schema(implementation = DioDataHolderClient.class))))})
  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_READ)
  default ResponseEntity<List<DioDataHolderClient>> listHolderClientes(
      @NotNull @Valid @PathVariable("holderId") UUID holderId) {
    return getDelegate().listHolderClients(holderId);
  }

  @Operation(summary = "Get a single Holder Client",
      description = "Returns a single holder client entry",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_READ})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_READ,
          content = @Content(schema = @Schema(implementation = DioDataHolderClient.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_NOT_FOUND,
          description = Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @GetMapping(value = "/{clientId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_READ)
  default ResponseEntity<DioDataHolderClient> getHolderClient(
      @NotNull @Valid @PathVariable("holderId") UUID holderId,
      @NotNull @Valid @PathVariable("clientId") UUID clientId) throws NotFoundException {
    return getDelegate().getHolderClient(holderId, clientId);
  }

  @Operation(summary = "Create a Holder Client",
      description = "Creates and Returns a new Holder Client",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_CREATED,
          description = Constants.RESPONSE_SUCCESSFUL_CREATE,
          content = @Content(schema = @Schema(implementation = DioDataHolderClient.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<DioDataHolderClient> createHolderClient(
      @NotNull @Valid @PathVariable("holderId") UUID holderId,
      @NotNull @RequestBody DioDataHolderClient client) throws ValidationListException, NotFoundException {
    return getDelegate().createHolderClient(holderId, client);
  }

  @Operation(summary = "Update a single Holder Client",
      description = "Updates and Returns an existing Holder Client",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})
  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_UPDATE,
          content = @Content(schema = @Schema(implementation = DioDataHolderClient.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_UNPROCESSABLE_ENTITY,
          description = Constants.RESPONSE_INPUT_VALIDATION_ERROR,
          content = @Content(array = @ArraySchema(
              schema = @Schema(implementation = ValidationListException.class))))})
  @PutMapping(path = "/{clientId}", consumes = {MediaType.APPLICATION_JSON_VALUE},
      produces = {MediaType.APPLICATION_JSON_VALUE})
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<DioDataHolderClient> updateHolderClient(
      @NotNull @Valid @PathVariable("holderId") UUID holderId,
      @NotNull @Valid @PathVariable("clientId") UUID clientId,
      @NotNull @RequestBody DioDataHolderClient holder) throws ValidationListException, NotFoundException {
    return getDelegate().updateHolderClient(holderId, clientId, holder);
  }

  @Operation(summary = "Delete a single HolderClient", description = "Deletes a HolderClient",
      security = {@SecurityRequirement(name = Constants.SECURITY_SCHEME_NAME,
          scopes = {Constants.SECURITY_SCOPE_HOLDER_WRITE})})

  @ApiResponses(value = {
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_OK,
          description = Constants.RESPONSE_SUCCESSFUL_DELETE,
          content = @Content(schema = @Schema(implementation = DioDataHolderClient.class))),
      @ApiResponse(responseCode = Constants.RESPONSE_CODE_NOT_FOUND,
          description = Constants.RESPONSE_OBJECT_NOT_FOUND)})
  @DeleteMapping(path = "/{clientId}")
  @PreAuthorize(Constants.OAUTH2_SCOPE_HOLDER_WRITE)
  default ResponseEntity<Void> deleteHolderClient(
      @NotNull @Valid @PathVariable("holderId") UUID holderId,
      @NotNull @Valid @PathVariable("clientId") UUID clientId) throws NotFoundException {
    return getDelegate().deleteHolderClient(holderId, clientId);
  }

}

