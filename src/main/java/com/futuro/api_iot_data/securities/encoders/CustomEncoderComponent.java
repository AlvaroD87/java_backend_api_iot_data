package com.futuro.api_iot_data.securities.encoders;

import org.springframework.stereotype.Component;

@Component
public class CustomEncoderComponent {
	
	private ICustomEncryptor encoder = new CustomEncryptorImpBCrypt();
	
	public ICustomEncryptor getEncoder() { return this.encoder; }
	
}
