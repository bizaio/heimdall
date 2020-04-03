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
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.babelfish.cdr.enumerations.oidc.CDRScope;
import io.biza.babelfish.cdr.enumerations.register.RegisterSoftwareRole;
import io.biza.babelfish.cdr.enumerations.register.SoftwareProductStatusType;
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
@Table(name = "SOFTWARE_PRODUCT")
public class SoftwareProductData {

  @Id
  @Column(name = "ID", updatable = false)
  @Type(type = "uuid-char")
  UUID id;

  @ManyToOne
  @JoinColumn(name = "DATA_RECIPIENT_BRAND_ID", nullable = false,
      foreignKey = @ForeignKey(name = "SOFTWARE_PRODUCT_DATA_RECIPIENT_BRAND_FK"))
  @ToString.Exclude
  @NotNull
  DataRecipientBrandData dataRecipientBrand;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  SoftwareProductStatusType status;

  @Column(name = "NAME")
  String name;

  @Column(name = "DESCRIPTION")
  @Lob
  String description;

  @Column(name = "URI")
  @Convert(converter = URIDataConverter.class)
  URI uri;

  @ElementCollection(targetClass = URI.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "SOFTWARE_PRODUCT_REDIRECT_URIS",
      joinColumns = @JoinColumn(name = "SOFTWARE_PRODUCT_ID"))
  @Column(name = "REDIRECT_URIS", nullable = false)
  @Convert(converter = URIDataConverter.class)  
  Set<URI> redirectUris;

  @Column(name = "LOGO_URI")
  @Convert(converter = URIDataConverter.class)
  URI logoUri;

  @Column(name = "TOS_URI")
  @Convert(converter = URIDataConverter.class)
  URI tosUri;

  @Column(name = "POLICY_URI")
  @Convert(converter = URIDataConverter.class)
  URI policyUri;

  @Column(name = "JWKS_URI")
  @Convert(converter = URIDataConverter.class)
  URI jwksUri;

  @Column(name = "REVOCATION_URI")
  @Convert(converter = URIDataConverter.class)
  URI revocationUri;

  @Column(name = "ROLE", nullable = false)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  RegisterSoftwareRole softwareRole = RegisterSoftwareRole.DATA_RECIPIENT_SOFTWARE_PRODUCT;

  @ElementCollection(targetClass = CDRScope.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "SOFTWARE_PRODUCT_SCOPE",
      joinColumns = @JoinColumn(name = "SOFTWARE_PRODUCT_ID"))
  @Column(name = "SCOPES", nullable = false)
  @Enumerated(EnumType.STRING)
  Set<CDRScope> scopes;

  @PrePersist
  public void prePersist() {
    if (dataRecipientBrand() != null) {
      Set<SoftwareProductData> products = new HashSet<SoftwareProductData>();
      if (dataRecipientBrand.softwareProducts() != null) {
        products.addAll(dataRecipientBrand.softwareProducts());
      }
      products.add(this);
      dataRecipientBrand.softwareProducts(products);
    }
  }

}
