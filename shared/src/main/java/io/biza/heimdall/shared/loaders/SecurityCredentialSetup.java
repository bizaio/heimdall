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
package io.biza.heimdall.shared.loaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.biza.babelfish.spring.exceptions.EncryptionOperationException;
import io.biza.babelfish.spring.exceptions.NotInitialisedException;
import io.biza.babelfish.spring.interfaces.CertificateService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SecurityCredentialSetup {
  @Autowired
  CertificateService certificateService;
  
  public void initialiseSecurityCredentials() {
    try {
      if(!certificateService.isInitialised()) {
        certificateService.initCa();
      }
    } catch (EncryptionOperationException | NotInitialisedException e) {
      LOG.error("Unable to initialise certificate authority: {}", e);
    }
  }
}
