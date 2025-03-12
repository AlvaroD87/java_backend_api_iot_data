package com.futuro.api_iot_data.securities.encoders;

public interface ICustomEncryptor {
	
	String encode(CharSequence rawPassword);
	
	boolean matches(CharSequence rawPassword, String encodedPassword);
}
