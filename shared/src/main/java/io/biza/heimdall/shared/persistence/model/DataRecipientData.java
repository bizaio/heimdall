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
package io.biza.heimdall.shared.persistence.model;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.babelfish.cdr.enumerations.register.IndustryType;
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
@Table(name = "DATA_RECIPIENT")
public class DataRecipientData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @OneToOne(mappedBy = "dataHolder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @NotNull
  LegalEntityData legalEntity;
  
  @Column(name = "REGISTER_INDUSTRY")
  @Enumerated(EnumType.STRING)
  IndustryType industry;
    
  @Column(name = "LOGO_URI")
  URI logoUri;
  
  @OneToMany(mappedBy = "dataRecipient", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<DataRecipientBrandData> dataRecipientBrands;
  
  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  DataRecipientStatusType status;
  
  @UpdateTimestamp
  @Column(name = "LAST_UPDATED")
  OffsetDateTime lastUpdated;
  
  @PrePersist
  public void prePersist() {
    if (dataRecipientBrands() != null) {
      for (DataRecipientBrandData one : dataRecipientBrands) {
        one.dataRecipient(this);
      }
    }
    if (legalEntity() != null) {
      legalEntity.dataRecipient(this);
    }
  }
  
}
