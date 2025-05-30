package com.futuro.api_iot_data.utils;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejador global de excepciones para la aplicación.
 * Esta clase captura excepciones lanzadas en los controladores y devuelve respuestas de error estructuradas
 * utilizando la clase {@link ErrorResponse}.
 * 
 * <p>Se utiliza la anotación {@link ControllerAdvice} para aplicar este manejador a todos los controladores.</p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de tipo {@link ResourceNotFoundException}.
     * Devuelve una respuesta con estado HTTP 404 (NOT FOUND) y un mensaje descriptivo del error.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con el estado HTTP 404 y un objeto {@link ErrorResponse}.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo {@link BadRequestInputException}.
     * Devuelve una respuesta con estado HTTP 400 (BAD REQUEST) y un mensaje descriptivo del error.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con el estado HTTP 400 y un objeto {@link ErrorResponse}.
     */
    @ExceptionHandler(BadRequestInputException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestInputException(BadRequestInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones genéricas no capturadas específicamente.
     * Devuelve una respuesta con estado HTTP 500 (INTERNAL SERVER ERROR) y un mensaje descriptivo del error.
     *
     * @param ex Excepción capturada.
     * @return ResponseEntity con el estado HTTP 500 y un objeto {@link ErrorResponse}.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    	return ResponseEntity
    			.status(HttpStatus.INTERNAL_SERVER_ERROR)
    			.body(new ErrorResponse(
    						HttpStatus.INTERNAL_SERVER_ERROR,
    						"Ocurrió un error interno en la aplicación, por favor contacte al administrador indicando path consultado, autenticación utilizada y body enviado."
    						)
    				);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException ex){
    	String errors = ex.getBindingResult()
    	        .getFieldErrors()
    	        .stream()
    	        .map(error -> error.getDefaultMessage())
    	        .collect(Collectors.joining("; "));
    	        
    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST, errors));
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handlerMissingServletRequestParameterException(MissingServletRequestParameterException ex){
    	return ResponseEntity
    			.status(HttpStatus.BAD_REQUEST)
    			.body(new ErrorResponse(
    						HttpStatus.BAD_REQUEST,
    						String.format("Parámetro requerido en path: %s (%s)", ex.getParameterName(), ex.getParameterType())
    					)
    			);
    }
}
