package com.EACH.Handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.EACH.exceptions.BadRequestException;
import com.EACH.exceptions.ExceptionResponse;
import com.EACH.exceptions.FileNotFoundException;
import com.EACH.exceptions.FileStorageException;
import com.EACH.exceptions.RequiredObjectIsNull;
import com.EACH.exceptions.ResourceNotFoundException;
import com.EACH.exceptions.UnsupportedMediaTypeException;

@ControllerAdvice
@RestController
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> 
	handleAllExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> 
	handleNotFoundExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(RequiredObjectIsNull.class)
	public final ResponseEntity<ExceptionResponse> 
	handleRequiredObjectExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(BadRequestException.class)
	public final ResponseEntity<ExceptionResponse> 
	handleBadRequestExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(FileNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> 
	handleFileNotFoundExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	@ExceptionHandler(FileStorageException.class)
	public final ResponseEntity<ExceptionResponse> 
	handleFileStorageExceptions(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	@ExceptionHandler(UnsupportedMediaTypeException.class)
	public final ResponseEntity<ExceptionResponse> 
	handleUnsupportedMediaTypeException(Exception ex, WebRequest request){
		ExceptionResponse exceptionResponse = 
				new ExceptionResponse
				(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
}
