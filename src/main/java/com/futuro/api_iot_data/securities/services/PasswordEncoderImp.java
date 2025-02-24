package com.futuro.api_iot_data.securities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.securities.encoders.CustomEncoderComponent;

@Service
public class PasswordEncoderImp implements PasswordEncoder{

	@Autowired
	CustomEncoderComponent customEncoder;
	
	@Override
	public String encode(CharSequence rawPassword) {
		return customEncoder.getEncoder().encode(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return customEncoder.getEncoder().matches(rawPassword, encodedPassword);
	}

}
