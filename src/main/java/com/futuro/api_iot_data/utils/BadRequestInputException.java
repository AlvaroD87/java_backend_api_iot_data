package com.futuro.api_iot_data.utils;

public class BadRequestInputException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7066799582494677122L;

	public BadRequestInputException(String message) {
        super(message);
    }
}