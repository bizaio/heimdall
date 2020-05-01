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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.biza.babelfish.cdr.Constants;
import io.biza.babelfish.cdr.exceptions.AttributeNotSupportedException;
import io.biza.babelfish.cdr.exceptions.NotInitialisedException;
import io.biza.babelfish.cdr.models.responses.ResponseErrorListV1;
import io.biza.babelfish.spring.controlleradvice.CdrExceptionControllerAdvice;

@ControllerAdvice
public class ExceptionControllerAdvice extends CdrExceptionControllerAdvice {

	@ExceptionHandler(NotInitialisedException.class)
	public ResponseEntity<Object> handleNotInitialised(HttpServletRequest req, AttributeNotSupportedException ex) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body(ResponseErrorListV1.builder().errors(List.of(Constants.ERROR_NOT_INITIALISED)).build());
	}
}
