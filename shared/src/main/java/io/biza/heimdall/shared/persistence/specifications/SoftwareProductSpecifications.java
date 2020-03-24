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

import java.util.UUID;
import javax.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData_;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import io.biza.heimdall.shared.persistence.model.DataRecipientData_;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData;
import io.biza.heimdall.shared.persistence.model.SoftwareProductData_;

public class SoftwareProductSpecifications {

  public static Specification<SoftwareProductData> recipientId(UUID recipientId) {
    return (root, query, cb) -> {
      Join<SoftwareProductData, DataRecipientBrandData> brandJoin =
          root.join(SoftwareProductData_.dataRecipientBrand);
      Join<DataRecipientBrandData, DataRecipientData> recipientJoin =
          brandJoin.join(DataRecipientBrandData_.dataRecipient);
      return cb.equal(recipientJoin.get(DataRecipientData_.id), recipientId);
    };
  }
}
