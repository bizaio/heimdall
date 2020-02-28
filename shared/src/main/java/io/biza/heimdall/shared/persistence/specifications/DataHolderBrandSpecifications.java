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
package io.biza.heimdall.shared.persistence.specifications;

import java.time.OffsetDateTime;
import org.springframework.data.jpa.domain.Specification;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData_;

public class DataHolderBrandSpecifications {

  public static Specification<DataHolderBrandData> updatedSince(OffsetDateTime updatedSince) {
    return (root, query, cb) -> {
      return cb.greaterThan(root.get(DataHolderBrandData_.lastUpdated), updatedSince);
    };
  }
}
