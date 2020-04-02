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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientBrandStatusType;
import io.biza.heimdall.shared.persistence.converter.URIDataConverter;
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
@Table(name = "DATA_RECIPIENT_BRAND")
public class DataRecipientBrandData {

  @Id
  @Column(name = "ID", updatable = false)
  @Type(type = "uuid-char")
  UUID id;

  @ManyToOne
  @JoinColumn(name = "DATA_RECIPIENT_ID", nullable = false,
      foreignKey = @ForeignKey(name = "DATA_RECIPIENT_BRAND_ID_FK"))
  @NotNull
  DataRecipientData dataRecipient;

  @Column(name = "BRAND_NAME")
  String brandName;

  @Column(name = "LOGO_URI")
  @Convert(converter = URIDataConverter.class)  
  URI logoUri;

  @OneToMany(mappedBy = "dataRecipientBrand", cascade = CascadeType.ALL)
  Set<SoftwareProductData> softwareProducts;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  DataRecipientBrandStatusType status;

  @PrePersist
  public void prePersist() {
    if (dataRecipient() != null) {
      Set<DataRecipientBrandData> brands = new HashSet<DataRecipientBrandData>();
      if (dataRecipient.dataRecipientBrands() != null) {
        brands.addAll(dataRecipient.dataRecipientBrands());
      }
      brands.add(this);
      dataRecipient.dataRecipientBrands(brands);
    }

    if (softwareProducts != null) {
      for (SoftwareProductData one : softwareProducts) {
        one.dataRecipientBrand(this);
      }
    }
  }

}
