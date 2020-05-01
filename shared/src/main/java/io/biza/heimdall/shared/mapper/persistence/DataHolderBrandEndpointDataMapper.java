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
package io.biza.heimdall.shared.mapper.persistence;

import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrandServiceEndpointV1;
import io.biza.babelfish.cdr.orika.OrikaFactoryConfigurerInterface;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandEndpointData;
import ma.glasnost.orika.MapperFactory;

public class DataHolderBrandEndpointDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory
        .classMap(DataHolderBrandEndpointData.class, RegisterDataHolderBrandServiceEndpointV1.class)
        .byDefault().register();
  }
}
