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
package io.biza.heimdall.data.persistence.model;

import java.net.URI;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Table;
import javax.validation.Valid;
import org.hibernate.annotations.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.biza.heimdall.payload.enumerations.DataRecipientStatusType;
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
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @ManyToOne
  @JoinColumn(name = "DATA_RECIPIENT_ID", nullable = false, foreignKey = @ForeignKey(name = "DATA_RECIPIENT_BRAND_ID_FK"))
  DataRecipientData dataRecipient;
  
  @Column(name = "BRAND_NAME")
  String brandName;
  
  @Column(name = "LOGO_URI")
  URI logoUri;
  
  @OneToMany(mappedBy = "dataRecipientBrand", cascade = CascadeType.ALL)
  Set<SoftwareProductData> softwareProducts;
  
  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  DataRecipientStatusType status;
  
}
