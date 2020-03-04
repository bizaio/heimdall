package io.biza.heimdall.shared.payloads.dio;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrandServiceEndpoint;
import io.biza.heimdall.shared.enumerations.DioClientCredentialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Valid
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "A Data Holder Client")
public class DioDataHolderClient {
  
  @JsonProperty("id")
  @NotNull
  @Schema(description = "Data Holder Client Identifier")
  UUID id;
  
  @JsonProperty("credentialType")
  @NotNull
  @Schema(description = "Client Credentialing Method")
  @Builder.Default
  DioClientCredentialType credentialType = DioClientCredentialType.CLIENT_CREDENTIALS_SECRET;
  
  @JsonProperty("clientSecret")
  @NotEmpty
  @Schema(
      description = "Client Secret")
  String clientSecret;
  
}
