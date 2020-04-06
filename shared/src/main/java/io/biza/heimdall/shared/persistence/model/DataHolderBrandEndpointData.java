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
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.register.CDRVersionType;
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
@Table(name = "DATA_HOLDER_BRAND_ENDPOINT")
public class DataHolderBrandEndpointData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "DATA_HOLDER_BRAND_ENDPOINT_ID",
      foreignKey = @ForeignKey(name = "ENDPOINT_DETAIL_DATA_HOLDER_BRAND_ID_FK"))
  DataHolderBrandData dataHolderBrand;

  @Column(name = "VERSION")
  @Enumerated(EnumType.STRING)
  CDRVersionType version;

  @Column(name = "PUBLIC_BASE_URI")
  URI publicBaseUri;

  @Column(name = "RESOURCE_BASE_URI")
  @Convert(converter = URIDataConverter.class)  
  URI resourceBaseUri;

  @Column(name = "INFOSEC_BASE_URI")
  @Convert(converter = URIDataConverter.class)  
  URI infosecBaseUri;

  @Column(name = "EXTENSION_BASE_URI")
  @Convert(converter = URIDataConverter.class)  
  URI extensionBaseUri;

  @Column(name = "WEBSITE_URI")
  @Convert(converter = URIDataConverter.class)  
  URI websiteUri;

  @PrePersist
  public void prePersist() {
    if (dataHolderBrand() != null) {
      dataHolderBrand.endpointDetail(this);
    }
  }

}
