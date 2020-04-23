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
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import io.biza.babelfish.cdr.enumerations.register.DataHolderStatusType;
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
@Table(name = "DATA_HOLDER_BRAND")
public class DataHolderBrandData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Column(name = "BRAND_NAME")
  @NotNull
  String brandName;

  @ManyToOne
  @JoinColumn(name = "DATA_HOLDER_ID", nullable = false,
      foreignKey = @ForeignKey(name = "DATA_HOLDER_BRAND_DATA_HOLDER_FK"))
  @ToString.Exclude
  DataHolderData dataHolder;

  @Column(name = "LOGO_URI")
  @NotNull
  @Convert(converter = URIDataConverter.class)
  URI logoUri;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  DataHolderStatusType status;

  @OneToOne(mappedBy = "dataHolderBrand", cascade = CascadeType.ALL, optional = true)
  DataHolderBrandEndpointData endpointDetail;

  @OneToMany(mappedBy = "dataHolderBrand", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<DataHolderBrandAuthData> authDetails;

  @UpdateTimestamp
  @Column(name = "LAST_UPDATED")
  OffsetDateTime lastUpdated;

  @PrePersist
  public void prePersist() {
    if (authDetails() != null) {
      for (DataHolderBrandAuthData one : authDetails()) {
        one.dataHolderBrand(this);
      }
    }

    if (dataHolder() != null) {
      Set<DataHolderBrandData> brands = new HashSet<DataHolderBrandData>();
      brands.addAll(dataHolder.dataHolderBrands());
      brands.add(this);
      dataHolder.dataHolderBrands(brands);
    }
    if (endpointDetail() != null) {
      endpointDetail.dataHolderBrand(this);
    }
  }


}
