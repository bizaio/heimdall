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
package io.biza.heimdall.register.api.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import io.biza.babelfish.spring.controlleradvice.OIDCExceptionControllerAdvice;

@ControllerAdvice
public class ExceptionControllerAdvice extends OIDCExceptionControllerAdvice {

}
