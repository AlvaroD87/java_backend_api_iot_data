package com.futuro.api_iot_data.securities.util;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Punto de entrada personalizado para manejar errores de autenticación en la API.
 * 
 * <p>Implementa {@link AuthenticationEntryPoint} de Spring Security para proporcionar
 * respuestas JSON consistentes cuando falla la autenticación.</p>
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Manejar todas las excepciones de autenticación no capturadas</li>
 *   <li>Proveer respuestas de error estandarizadas en formato JSON</li>
 *   <li>Diferenciar entre tipos de errores de autenticación</li>
 * </ul>
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	/**
     * Maneja una solicitud de autenticación fallida.
     * 
     * @param request Solicitud HTTP que causó la excepción
     * @param response Respuesta HTTP que será enviada al cliente
     * @param authException Excepción que causó el fallo
     * @throws IOException Si ocurre un error de I/O al escribir la respuesta
     * @throws ServletException Si ocurre un error general del servlet
     */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		
		String message = authException instanceof InsufficientAuthenticationException
							? "Credenciales invalidas o inexistentes, verifique el metodo correcto de autenticación."
							: authException.getMessage();
		
		response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format("{\"status\": 401, \"message\": \"%s\"}", message));
		
	}

}
