package io.biza.heimdall.admin.model.payloads;

import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.converters.CountryStringToLocaleConverter;
import io.biza.babelfish.cdr.converters.LocalDateToStringConverter;
import io.biza.babelfish.cdr.converters.LocaleToCountryStringConverter;
import io.biza.babelfish.cdr.converters.StringToLocalDateConverter;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.heimdall.data.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.payload.enumerations.DataRecipientStatusType;
import io.biza.heimdall.payload.enumerations.IndustryType;
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
  
  @JsonProperty("name")
  @NotEmpty
  @Schema(
      description = "Unique legal name of the organisation")
  String name;
  
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
