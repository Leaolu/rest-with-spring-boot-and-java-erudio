package com.EACH.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnAuthoException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnAuthoException(String ex) {
		super(ex);
	}
}
