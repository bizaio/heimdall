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

import io.biza.babelfish.cdr.models.payloads.register.recipient.DataRecipientStatus;
import io.biza.babelfish.cdr.models.payloads.register.recipient.RegisterDataRecipient;
import io.biza.heimdall.shared.mapper.OrikaFactoryConfigurerInterface;
import io.biza.heimdall.shared.payloads.dio.DioDataRecipient;
import io.biza.heimdall.shared.persistence.model.DataRecipientData;
import ma.glasnost.orika.MapperFactory;

public class DataRecipientDataMapper implements OrikaFactoryConfigurerInterface {

  @Override
  public void configure(MapperFactory orikaMapperFactory) {
    orikaMapperFactory.classMap(DataRecipientData.class, DataRecipientStatus.class)
    .fieldAToB("id", "dataRecipientId")
    .field("status", "dataRecipientStatus")
    .byDefault()
    .register();
    
    orikaMapperFactory.classMap(DataRecipientData.class, RegisterDataRecipient.class)
    .fieldAToB("legalEntity.id", "legalEntityId")
    .field("legalEntity.legalName", "legalEntityName")
    .byDefault()
    .register();
    
    orikaMapperFactory.classMap(DataRecipientData.class, DioDataRecipient.class)
    .fieldAToB("id", "id")
    .byDefault()
    .register();

  }
}
