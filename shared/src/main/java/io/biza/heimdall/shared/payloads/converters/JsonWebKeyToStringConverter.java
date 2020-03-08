package io.biza.heimdall.shared.payloads.converters;

import org.jose4j.jwk.JsonWebKey;
import com.fasterxml.jackson.databind.util.StdConverter;

public class JsonWebKeyToStringConverter extends StdConverter<JsonWebKey, String> {

  @Override
  public String convert(JsonWebKey value) {
      return value.toJson();
  }
}
