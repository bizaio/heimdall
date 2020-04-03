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
@Table(name = "DATA_HOLDER")
public class DataHolderData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @Column(name = "NAME")
  @NotNull
  String name;

  @OneToOne(mappedBy = "dataHolder", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @NotNull
  LegalEntityData legalEntity;

  @Column(name = "REGISTER_INDUSTRY")
  @Enumerated(EnumType.STRING)
  IndustryType industry;

  @OneToMany(mappedBy = "dataHolder", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<DataHolderBrandData> dataHolderBrands;

  @PrePersist
  public void prePersist() {
    if (dataHolderBrands() != null) {
      for (DataHolderBrandData one : dataHolderBrands) {
        one.dataHolder(this);
      }
    }

    if (legalEntity() != null) {
      legalEntity.dataHolder(this);
    }
  }
}
