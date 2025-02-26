package com.futuro.api_iot_data.utils;

public class ResourceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7066799582494677122L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}