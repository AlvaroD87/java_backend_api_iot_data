package com.futuro.api_iot_data.securities.util;

import org.springframework.security.core.AuthenticationException;

/**
 * Excepción personalizada para fallos en el proceso de autenticación.
 * 
 * <p>Extiende {@link AuthenticationException} de Spring Security para representar
 * errores específicos de autenticación en la aplicación.</p>
 * 
 * <p><strong>Casos de uso típicos:</strong></p>
 * <ul>
 *   <li>Credenciales inválidas</li>
 *   <li>Cuenta deshabilitada</li>
 *   <li>Restricciones de acceso específicas de la aplicación</li>
 * </ul>
 */
public class AuthenticationFailException extends AuthenticationException{

	private static final long serialVersionUID = 1L;

	/**
     * Crea una nueva instancia con un mensaje de error descriptivo.
     * 
     * @param msg Mensaje detallando la causa del fallo de autenticación
     */
	public AuthenticationFailException(String msg) {
		super(msg);
	}
	
}
