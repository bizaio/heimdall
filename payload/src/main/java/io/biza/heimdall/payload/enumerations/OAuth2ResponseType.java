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
package io.biza.heimdall.payload.enumerations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import io.biza.babelfish.cdr.exceptions.LabelValueEnumValueNotSupportedException;
import io.biza.babelfish.cdr.support.LabelValueEnumInterface;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "OIDC Response Types", enumAsRef = true)
public enum OAuth2ResponseType implements LabelValueEnumInterface {
  // @formatter:off
  CODE("CODE", "code"),
  ID_TOKEN("ID_TOKEN", "id_token"),
  TOKEN("TOKEN", "token");
  // @formatter:on

  private String value;

  private String label;

  OAuth2ResponseType(String value, String label) {
    this.value = value;
    this.label = label;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static OAuth2ResponseType fromValue(String text)
      throws LabelValueEnumValueNotSupportedException {
    for (OAuth2ResponseType b : OAuth2ResponseType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    throw new LabelValueEnumValueNotSupportedException(
        "Unable to identify value of OAuth2ResponseType from " + text,
        OAuth2ResponseType.class.getSimpleName(), OAuth2ResponseType.values(),
        text);
  }

  @Override
  @JsonIgnore
  public String label() {
    return label;
  }
}