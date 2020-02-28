package io.biza.heimdall.shared.payloads.converters;

import java.io.IOException;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class JWKJsonDeserializer extends StdDeserializer<JsonWebKey> {
  private static final long serialVersionUID = 1L;

  public JWKJsonDeserializer() {
      super(JsonWebKey.class);
  }

  @Override
  public JsonWebKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
      try {
          return JsonWebKey.Factory.newJwk(jsonParser.readValueAs(String.class));
      } catch (JoseException e) {
          throw new JsonParseException(jsonParser, "Unable to parse Json Web Key");
      }
  }
}