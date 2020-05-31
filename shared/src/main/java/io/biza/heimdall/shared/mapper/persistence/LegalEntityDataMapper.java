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

import io.biza.babelfish.cdr.models.payloads.register.common.LegalEntityDetailV1;
import io.biza.babelfish.converter.orika.OrikaFactoryConfigurerInterface;
import io.biza.heimdall.shared.payloads.dio.DioLegalEntity;
import io.biza.heimdall.shared.persistence.model.LegalEntityData;
import ma.glasnost.orika.MapperFactory;

public class LegalEntityDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(LegalEntityData.class, DioLegalEntity.class)
        .field("legalName", "name").byDefault().register();

    orikaMapperFactory.classMap(LegalEntityData.class, LegalEntityDetailV1.class)
        .fieldAToB("id", "legalEntityId").field("legalName", "legalEntityName").byDefault()
        .register();

  }
}