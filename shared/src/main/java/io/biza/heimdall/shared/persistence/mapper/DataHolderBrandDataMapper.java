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

import io.biza.babelfish.cdr.models.payloads.register.holder.RegisterDataHolderBrand;
import io.biza.babelfish.cdr.orika.OrikaFactoryConfigurerInterface;
import io.biza.heimdall.shared.payloads.dio.DioDataHolderBrand;
import io.biza.heimdall.shared.persistence.model.DataHolderBrandData;
import ma.glasnost.orika.MapperFactory;

public class DataHolderBrandDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(DataHolderBrandData.class, RegisterDataHolderBrand.class)
    .fieldAToB("id", "dataHolderBrandId")
    .field("dataHolder.industry", "industry")
    .field("dataHolder.legalEntity", "legalEntity")
    .byDefault()
    .register();
    
    orikaMapperFactory.classMap(DataHolderBrandData.class, DioDataHolderBrand.class)
    .fieldAToB("id", "id")
    .field("brandName", "name")
    .byDefault()
    .register();

  }
}
