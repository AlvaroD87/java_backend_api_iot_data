package com.futuro.api_iot_data.utils;

/**
 * Excepción personalizada que representa un error cuando un recurso no se encuentra.
 * Esta excepción se lanza cuando se intenta acceder a un recurso que no existe en el sistema.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7066799582494677122L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}