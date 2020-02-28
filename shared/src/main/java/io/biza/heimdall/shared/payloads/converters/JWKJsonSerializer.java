package io.biza.heimdall.shared.payloads.converters;

import java.io.IOException;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.JsonWebKey.OutputControlLevel;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class JWKJsonSerializer extends StdSerializer<JsonWebKey> {
  private static final long serialVersionUID = 1L;

  public JWKJsonSerializer() {
      super(JsonWebKey.class);
  }

  @Override
  public void serialize(JsonWebKey jsonWebKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
      jsonGenerator.writeObject(jsonWebKey.toParams(OutputControlLevel.INCLUDE_SYMMETRIC));
  }
}