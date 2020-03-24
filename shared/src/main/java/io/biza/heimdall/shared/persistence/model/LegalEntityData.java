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

import java.time.LocalDate;
import java.util.Locale;
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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.CommonOrganisationType;
import io.biza.heimdall.shared.Constants;
import io.biza.heimdall.shared.persistence.converter.LocaleDataConverter;
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
@Table(name = "LEGAL_ENTITY")
public class LegalEntityData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DATA_HOLDER_ID",
      foreignKey = @ForeignKey(name = "LEGAL_ENTITY_DATA_HOLDER_ID_FK"))
  @ToString.Exclude
  DataHolderData dataHolder;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DATA_RECIPIENT_ID",
      foreignKey = @ForeignKey(name = "LEGAL_ENTITY_DATA_RECIPIENT_ID_FK"))
  @ToString.Exclude
  DataRecipientData dataRecipient;

  @Column(name = "LEGAL_NAME")
  @NotNull
  String legalName;

  @Column(name = "REGISTRATION_NUMBER")
  String registrationNumber;

  @Column(name = "REGISTRATION_DATE")
  LocalDate registrationDate;

  @Column(name = "REGISTERED_COUNTRY")
  @Convert(converter = LocaleDataConverter.class)
  @NotNull
  @Builder.Default
  Locale registeredCountry = new Locale(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_LOCALE);

  @Column(name = "ABN")
  String abn;

  @Column(name = "ACN")
  String acn;

  @Column(name = "ARBN")
  String arbn;

  @Column(name = "ANZSIC_CODE")
  String industryCode;

  @Column(name = "ORGANISATION_TYPE")
  @Enumerated(EnumType.STRING)
  @NotNull
  CommonOrganisationType organisationType;

  @PrePersist
  public void prePersist() {
    if (dataHolder() != null) {
      dataHolder().legalEntity(this);
    }
    if (dataRecipient() != null) {
      dataRecipient().legalEntity(this);
    }

  }

}
