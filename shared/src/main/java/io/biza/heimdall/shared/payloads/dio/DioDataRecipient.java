package io.biza.heimdall.shared.payloads.dio;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
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
@Schema(description = "The data that is common to all organisations, regardless of the type (e.g. company, trust, partnership, government)")
public class DioDataRecipient {
  
  @JsonProperty("id")
  @NotNull
  @Schema(description = "Data Holder Identifier")
  UUID id;
  
  @JsonProperty("legalEntity")
  @NotNull
  @Schema(description = "Legal Entity details for Data Holder")
  DioLegalEntity legalEntity;
  
  @JsonProperty("industry")
  @NotNull
  IndustryType industry;
    
  @JsonProperty("logoUri")
  @NotNull
  URI logoUri;
  
  @JsonProperty("status")
  @NotNull
  DataRecipientStatusType status;
  
  @JsonProperty("lastUpdated")
  OffsetDateTime lastUpdated;

}
