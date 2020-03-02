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

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Type;
import io.biza.heimdall.payload.enumerations.JWKStatus;
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
@Table(name = "REGISTER_JWK")
public class RegisterAuthorityJWKData {

  @Id
  @Column(name = "ID", insertable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Type(type = "uuid-char")
  UUID id;
  
  @Column(name = "JAVA_FACTORY")
  @NotNull
  String javaFactory;
  
  @Column(name = "JOSE_ALGORITHM")
  @NotNull
  String joseAlgorithm;
  
  @Column(name = "PUBLIC_KEY")
  @NotNull
  @Lob
  String publicKey;
  
  @Column(name = "PRIVATE_KEY")
  @NotNull
  @Lob
  String privateKey;
  
  @Column(name = "STATUS")
  @NotNull
  @Builder.Default
  @Enumerated(EnumType.STRING)
  JWKStatus status = JWKStatus.ACTIVE;
  
  
}
