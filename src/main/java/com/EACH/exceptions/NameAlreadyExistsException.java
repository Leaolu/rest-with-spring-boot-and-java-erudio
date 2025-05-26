package com.EACH.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NameAlreadyExistsException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public NameAlreadyExistsException(String ex) {
		super(ex);
	}
	public NameAlreadyExistsException() {
		super("ERROR: this username already exists, please enter a different one!");
	}
}
