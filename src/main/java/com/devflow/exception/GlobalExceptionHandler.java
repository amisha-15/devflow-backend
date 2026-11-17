package com.devflow.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(
		MethodArgumentNotValidException exception){
			
		Map<String,String> errors = new HashMap<>();
		
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		
}
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, String>> handleIllegalArgument(
	        IllegalArgumentException exception) {

	    Map<String, String> error = new HashMap<>();
	    error.put("error", exception.getMessage());

	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, Object>> handleResponseStatusException(
	        ResponseStatusException ex) {

	    Map<String, Object> response = Map.of(
	            "status", ex.getStatusCode().value(),
	            "message", ex.getReason()
	    );

	    return ResponseEntity
	            .status(ex.getStatusCode())
	            .body(response);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, String>> handleInvalidJson(
	        HttpMessageNotReadableException exception) {

	    Map<String, String> error = new HashMap<>();

	    error.put("error", "Invalid request body. Check your JSON format and enum values.");

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}
