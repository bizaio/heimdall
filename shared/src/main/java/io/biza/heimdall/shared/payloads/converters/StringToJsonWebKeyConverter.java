package io.biza.heimdall.shared.payloads.converters;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import com.fasterxml.jackson.databind.util.StdConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToJsonWebKeyConverter extends StdConverter<String, JsonWebKey> {

  @Override
  public JsonWebKey convert(String value) {
    try {
      return JsonWebKey.Factory.newJwk(value);
    } catch (JoseException e) {
      LOG.error("Unable to parse JWK from String of {}", value);
      return null;
    }
  }
}
