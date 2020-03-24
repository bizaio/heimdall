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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.heimdall.shared.enumerations.DioClientCredentialType;
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
@Table(name = "CLIENT")
public class ClientData {

  @Id
  @Column(name = "ID", updatable = false)
  @Type(type = "uuid-char")
  UUID id;

  @Column(name = "CREDENTIAL_TYPE")
  @NotNull
  @Enumerated(EnumType.STRING)
  DioClientCredentialType credentialType;

  @ManyToOne
  @JoinColumn(name = "DATA_HOLDER_ID", foreignKey = @ForeignKey(name = "CLIENT_DATA_HOLDER_FK"))
  @ToString.Exclude
  DataHolderData dataHolder;

  @ManyToOne
  @JoinColumn(name = "SOFTWARE_PRODUCT_ID",
      foreignKey = @ForeignKey(name = "CLIENT_SOFTWARE_PRODUCT_FK"))
  @ToString.Exclude
  SoftwareProductData softwareProduct;

  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
  @ToString.Exclude
  Set<TokenData> tokens;

  @Column(name = "CLIENT_SECRET")
  String clientSecret;

  @AssertTrue
  private Boolean isClientSecretRequiredAndPopulated() {
    return credentialType.equals(DioClientCredentialType.CLIENT_CREDENTIALS_SECRET)
        && clientSecret != null;
  }

  @AssertTrue
  private Boolean isHolderOrProductPopulated() {
    return (dataHolder != null && softwareProduct == null)
        || (dataHolder == null && softwareProduct != null);
  }

  @PrePersist
  public void prePersist() {
    if (dataHolder() != null) {
      Set<ClientData> clients = new HashSet<ClientData>();
      clients.addAll(dataHolder.clients());
      clients.add(this);
      dataHolder.clients(clients);
    }

    if (softwareProduct() != null) {
      Set<ClientData> clients = new HashSet<ClientData>();
      clients.addAll(softwareProduct.clients());
      clients.add(this);
      softwareProduct.clients(clients);
    }
  }


}
