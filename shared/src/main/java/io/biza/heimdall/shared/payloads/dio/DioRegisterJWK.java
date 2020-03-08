package io.biza.heimdall.shared.payloads.dio;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import org.jose4j.jwk.JsonWebKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.shared.payloads.converters.JsonWebKeyToStringConverter;
import io.biza.heimdall.shared.payloads.converters.StringToJsonWebKeyConverter;
import io.swagger.v3.oas.annotations.media.Schema;

public class DioRegisterJWK {

  @JsonProperty("id")
  @NotNull
  @Schema(description = "Register JWK Identifier")
  UUID id;
  
  @JsonSerialize(converter = JsonWebKeyToStringConverter.class)
  @JsonDeserialize(converter = StringToJsonWebKeyConverter.class)
  JsonWebKey jwk;
  
  @JsonProperty("status")
  @NotNull
  JWKStatus status;
  
}
