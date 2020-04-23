/*******************************************************************************
 * Copyright (C) 2020 Biza Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *******************************************************************************/
package io.biza.heimdall.shared.persistence.model;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.register.RegisterAuthType;
import io.biza.babelfish.spring.persistence.converter.URIDataConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Valid
@Table(name = "DATA_HOLDER_BRAND_AUTH")
public class DataHolderBrandAuthData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @ManyToOne
  @JoinColumn(name = "DATA_HOLDER_BRAND_ID", nullable = false,
      foreignKey = @ForeignKey(name = "DATA_HOLDER_BRAND_AUTH_DATA_HOLDER_BRAND_FK"))
  DataHolderBrandData dataHolderBrand;

  @Column(name = "AUTH_TYPE")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  RegisterAuthType authType = RegisterAuthType.SIGNED_JWT;

  @Column(name = "JWKS_ENDPOINT")
  @NotNull
  @Convert(converter = URIDataConverter.class)
  URI jwksEndpoint;

  @PrePersist
  public void prePersist() {
    if (dataHolderBrand() != null) {
      Set<DataHolderBrandAuthData> authData = new HashSet<DataHolderBrandAuthData>();
      authData.addAll(dataHolderBrand.authDetails());
      authData.add(this);
      dataHolderBrand.authDetails(authData);
    }
  }

}
