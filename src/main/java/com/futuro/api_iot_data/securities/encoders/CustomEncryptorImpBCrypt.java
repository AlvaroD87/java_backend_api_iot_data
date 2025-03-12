package com.futuro.api_iot_data.securities.encoders;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class CustomEncryptorImpBCrypt implements ICustomEncryptor{

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	@Override
	public String encode(CharSequence rawPassword) {
		return encoder.encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}

}
