package com.futuro.api_iot_data.utils;

/**
 * Excepción personalizada que representa un error de solicitud incorrecta (Bad Request).
 * Esta excepción se lanza cuando los datos de entrada proporcionados no cumplen con las expectativas
 * o no son válidos según las reglas de negocio.
 */
public class BadRequestInputException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7066799582494677122L;

	public BadRequestInputException(String message) {
        super(message);
    }
}