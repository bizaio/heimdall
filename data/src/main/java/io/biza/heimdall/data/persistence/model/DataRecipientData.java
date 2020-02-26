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
import java.time.OffsetDateTime;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.biza.heimdall.payload.enumerations.DataRecipientStatusType;
import io.biza.heimdall.payload.enumerations.IndustryType;
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
public class DataRecipientData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @OneToOne(mappedBy = "dataRecipient", cascade = CascadeType.ALL)
  @NotNull
  LegalEntityData legalEntity;
  
  @Column(name = "REGISTER_INDUSTRY")
  @Enumerated(EnumType.STRING)
  IndustryType industry;
    
  @Column(name = "LOGO_URI")
  URI logoUri;
  
  @OneToMany(mappedBy = "dataRecipient", cascade = CascadeType.ALL)
  Set<DataRecipientBrandData> dataRecipientBrands;
  
  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  DataRecipientStatusType status;
  
  @UpdateTimestamp
  @Column(name = "LAST_UPDATED")
  OffsetDateTime lastUpdated;
  
  
}