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
@Schema(description = "Data Recipient Brand Software Products")
public class DioSoftwareProduct {
  
  @JsonProperty("id")
  @NotNull
  @Schema(description = "Data Recipient Brand Identifier")
  UUID id;
  
  @JsonProperty("name")
  @NotEmpty
  @Schema(
      description = "Software Product Name")
  String name;
  
  @JsonProperty("description")
  @NotEmpty
  @Schema(
      description = "Software Product Description")
  String description;
  
  @JsonProperty("logoUri")
  @NotNull
  URI logoUri;
  
  @JsonProperty("status")
  @NotNull
  DataRecipientStatusType status;
  
}
