package io.biza.heimdall.shared.payloads.dio;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.jose4j.jwk.JsonWebKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.shared.payloads.converters.JWKJsonDeserializer;
import io.biza.heimdall.shared.payloads.converters.JWKJsonSerializer;
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
@Schema(description = "A Register Certificate")
public class DioRegisterCertificate {

  @JsonProperty("id")
  @NotNull
  @Schema(description = "Register JWK Identifier")
  UUID id;
  
  @JsonSerialize(using = JWKJsonSerializer.class)
  @JsonDeserialize(using = JWKJsonDeserializer.class)
  public JsonWebKey jwk;
  
  @JsonProperty("status")
  @NotNull
  JWKStatus status;
  
}
