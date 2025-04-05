package com.futuro.api_iot_data.securities.util;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationFailException extends AuthenticationException{

	private static final long serialVersionUID = 1L;

	public AuthenticationFailException(String msg) {
		super(msg);
	}
	
}
