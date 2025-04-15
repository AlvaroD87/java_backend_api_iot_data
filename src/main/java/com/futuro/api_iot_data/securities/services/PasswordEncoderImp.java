package com.futuro.api_iot_data.securities.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.securities.encoders.CustomEncoderComponent;

/**
 * Implementación del servicio de codificación de contraseñas de Spring Security.
 * 
 * <p>Actúa como adaptador entre el {@link PasswordEncoder} de Spring Security
 * y el sistema de codificación personalizado ({@link CustomEncoderComponent}).</p>
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Proveer codificación de contraseñas compatible con Spring Security</li>
 *   <li>Delegar las operaciones de cifrado al componente personalizado</li>
 *   <li>Servir como puente entre Spring Security y la implementación específica</li>
 * </ul>
 */
@Service
public class PasswordEncoderImp implements PasswordEncoder{

	@Autowired
	CustomEncoderComponent customEncoder;
	
	/**
     * Codifica una contraseña en texto plano usando el codificador configurado.
     * 
     * @param rawPassword Contraseña en texto plano a codificar
     * @return String con la contraseña cifrada
     * @throws IllegalArgumentException si rawPassword es null
     */
	@Override
	public String encode(CharSequence rawPassword) {
		return customEncoder.getEncoder().encode(rawPassword);
	}

	/**
     * Verifica si una contraseña en texto plano coincide con una versión codificada.
     * 
     * @param rawPassword Contraseña en texto plano a verificar
     * @param encodedPassword Contraseña codificada almacenada
     * @return true si coinciden, false en caso contrario
     * @throws IllegalArgumentException si alguno de los parámetros es null
     */
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return customEncoder.getEncoder().matches(rawPassword, encodedPassword);
	}

}
