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
package io.biza.heimdall.shared.persistence.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.biza.babelfish.cdr.enumerations.register.JWKStatus;
import io.biza.heimdall.shared.persistence.model.RegisterAuthorityJWKData;

@Repository
public interface RegisterAuthorityJWKRepository extends JpaRepository<RegisterAuthorityJWKData, UUID> {
  RegisterAuthorityJWKData findFirstByStatusIn(List<JWKStatus> status);
  List<RegisterAuthorityJWKData> findByStatusIn(List<JWKStatus> status);
}
