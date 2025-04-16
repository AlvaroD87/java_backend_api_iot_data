package com.futuro.api_iot_data.securities.encoders;

/**
 * Interfaz que define el contrato para los componentes de encriptación de contraseñas.
 * 
 * <p>Proporciona una abstracción para:</p>
 * <ul>
 *   <li>Codificación segura de contraseñas</li>
 *   <li>Verificación de contraseñas contra hashes almacenados</li>
 * </ul>
 * 
 * <p><strong>Propósito:</strong></p>
 * <ul>
 *   <li>Permitir múltiples implementaciones de algoritmos de cifrado</li>
 *   <li>Establecer una interfaz común para el manejo de credenciales</li>
 *   <li>Facilitar el cumplimiento de estándares de seguridad</li>
 * </ul>
 */
public interface ICustomEncryptor {
	
	/**
     * Codifica una contraseña en texto plano usando BCrypt.
     * 
     * @param rawPassword Contraseña en texto plano a codificar
     * @return String con la contraseña cifrada (hash BCrypt)
     */
	String encode(CharSequence rawPassword);
	
	/**
     * Verifica si una contraseña en texto plano coincide con un hash BCrypt almacenado.
     * 
     * @param rawPassword Contraseña en texto plano a verificar
     * @param encodedPassword Hash BCrypt almacenado para comparación
     * @return true si la contraseña coincide, false en caso contrario
     */
	boolean matches(CharSequence rawPassword, String encodedPassword);
}
