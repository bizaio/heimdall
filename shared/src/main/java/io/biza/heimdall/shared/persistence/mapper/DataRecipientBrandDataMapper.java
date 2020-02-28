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
package io.biza.heimdall.shared.persistence.mapper;

import io.biza.heimdall.payload.holder.RegisterDataHolderAuth;
import io.biza.heimdall.payload.holder.RegisterDataHolderBrand;
import io.biza.heimdall.payload.holder.RegisterDataHolderBrandServiceEndpoint;
import io.biza.heimdall.payload.recipient.DataRecipientBrandMetaData;
import io.biza.heimdall.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandAuthData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandEndpointData;
import io.biza.heimdall.shared.persistence.model.DataHolderData;
import io.biza.heimdall.shared.persistence.model.DataRecipientBrandData;
import ma.glasnost.orika.MapperFactory;

public class DataRecipientBrandDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(DataRecipientBrandData.class, DataRecipientBrandMetaData.class)
    .fieldAToB("id", "dataRecipientBrandId")
    .byDefault()
    .register();

  }
}
