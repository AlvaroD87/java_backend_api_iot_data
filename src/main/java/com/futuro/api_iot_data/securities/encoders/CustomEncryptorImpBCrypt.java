package com.futuro.api_iot_data.securities.encoders;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Implementación de {@link ICustomEncryptor} que utiliza el algoritmo BCrypt para el cifrado de contraseñas.
 * 
 */
public class CustomEncryptorImpBCrypt implements ICustomEncryptor{

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	
	/**
     * Codifica una contraseña en texto plano usando BCrypt.
     * 
     * @param rawPassword Contraseña en texto plano a codificar
     * @return String con la contraseña cifrada (hash BCrypt)
     */
	@Override
	public String encode(CharSequence rawPassword) {
		return encoder.encode(rawPassword);
	}

	/**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt almacenado.
     * 
     * @param rawPassword Contraseña en texto plano a verificar
     * @param encodedPassword Hash BCrypt almacenado para comparación
     * @return true si la contraseña coincide, false en caso contrario
     */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}

}
