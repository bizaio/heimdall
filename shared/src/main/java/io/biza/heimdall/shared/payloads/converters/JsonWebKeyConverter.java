/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package io.biza.heimdall.shared.payloads.converters;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@Service
@Slf4j
public class JsonWebKeyConverter extends BidirectionalConverter<JsonWebKey,String> {
  
  @Override
  public String convertTo(JsonWebKey source, Type<String> destinationType,
      MappingContext mappingContext) {
    return source.toJson();
  }

  @Override
  public JsonWebKey convertFrom(String source, Type<JsonWebKey> destinationType,
      MappingContext mappingContext) {
    try {
      return JsonWebKey.Factory.newJwk(source);
    } catch (JoseException e) {
      LOG.error("Unable to convert string to JsonWebKey");
      return null;
    }
  }

}