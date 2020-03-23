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
package io.biza.heimdall.shared.persistence.specifications;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import io.biza.heimdall.shared.persistence.model.ClientData;
import io.biza.heimdall.shared.persistence.model.ClientData_;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData_;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.DataHolderData_;

public class ClientSpecifications {

  public static Specification<ClientData> holderId(UUID holderId) {
    return (root, query, cb) -> {
      Join<ClientData, DataHolderData> holderJoin = root.join(ClientData_.dataHolder);
      return cb.equal(holderJoin.get(DataHolderData_.id), holderId);
    };
  }
}
