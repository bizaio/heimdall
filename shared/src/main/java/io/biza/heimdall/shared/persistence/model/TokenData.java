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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.biza.babelfish.cdr.enumerations.register.DataHolderStatusType;
import io.biza.babelfish.cdr.enumerations.register.DataRecipientStatusType;
import io.biza.heimdall.shared.enumerations.HeimdallTokenType;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Valid
@Table(name = "TOKEN")
public class TokenData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @ManyToOne
  @JoinColumn(name = "CLIENT_ID", nullable = true, foreignKey = @ForeignKey(name = "TOKEN_CLIENT_ID_FK"))
  @ToString.Exclude
  ClientData client;

  @Column(name = "TOKEN_TYPE")
  @Enumerated(EnumType.STRING)
  HeimdallTokenType tokenType;
    
  @Column(name = "EXPIRY")
  OffsetDateTime expiry;
  
  @Column(name = "CREATED")
  @CreationTimestamp
  OffsetDateTime created;
  
  @Column(name = "AUDIENCE")
  String audience;

  @Column(name = "AUTHENTICATION_TIME")
  OffsetDateTime authenticationTime;
  
  @Column(name = "SCOPE")
  @ElementCollection
  @NotNull
  List<String> scopes;
  
}
