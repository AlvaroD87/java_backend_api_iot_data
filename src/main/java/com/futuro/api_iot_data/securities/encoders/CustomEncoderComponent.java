package com.futuro.api_iot_data.securities.encoders;

import org.springframework.stereotype.Component;

/**
 * Proporciona un servicio de encriptación configurable.
 * 
 * <p>Actúa como contenedor para la implementación de encriptación, permitiendo
 * una fácil sustitución del algoritmo de encriptación.</p>
 * 
 * <p>Por defecto utiliza la implementación BCrypt para el cifrado de datos.</p>
 * 
 * @see ICustomEncryptor
 * @see CustomEncryptorImpBCrypt
 */
@Component
public class CustomEncoderComponent {
	
	private ICustomEncryptor encoder = new CustomEncryptorImpBCrypt();
	
	/**
     * Obtiene la implementación actual del encriptador.
     * 
     * @return Instancia de {@link ICustomEncryptor} configurada como encriptador principal
     */
	public ICustomEncryptor getEncoder() { return this.encoder; }
	
}
