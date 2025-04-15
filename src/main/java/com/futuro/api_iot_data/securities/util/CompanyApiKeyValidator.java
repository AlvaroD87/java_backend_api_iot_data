package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro personalizado para validar API Keys de compañías en las solicitudes HTTP.
 * 
 * <p>Este filtro extiende {@link OncePerRequestFilter} de Spring para validar
 * las API Keys incluidas en los headers de las peticiones.</p>
 * 
 * <p><strong>Funcionamiento:</strong></p>
 * <ol>
 *   <li>Verifica si la ruta requiere validación de API Key</li>
 *   <li>Extrae la API Key del header 'api-key'</li>
 *   <li>Valida la clave contra el caché de claves autorizadas</li>
 *   <li>Establece el contexto de seguridad si la validación es exitosa</li>
 *   <li>Rechaza la petición con un error 401 si la validación falla</li>
 * </ol>
 */
public class CompanyApiKeyValidator extends OncePerRequestFilter{

	private ApiKeysCacheData apiKeysCacheData;
	private Map<String,List<String>> pathsToApplyFilter;	
	private AuthenticationEntryPoint failureHandler;
	
	/**
     * Constructor del filtro.
     * 
     * @param apiKeysCacheData Caché de claves API válidas
     * @param pathsToApplyFilter Mapa de rutas y métodos HTTP que requieren validación
     * @param failureHandler Manejador para respuestas de autenticación fallida
     */
	public CompanyApiKeyValidator(ApiKeysCacheData apiKeysCacheData, Map<String,List<String>> pathsToApplyFilter, AuthenticationEntryPoint failureHandler) {	
		this.apiKeysCacheData = apiKeysCacheData;
		this.pathsToApplyFilter = pathsToApplyFilter;
		this.failureHandler = failureHandler;
	}
	
	 /**
     * Lógica principal del filtro.
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String headerCompanyApiKey = request.getHeader("api-key");
		System.out.println(apiKeysCacheData.isValidCompanyApiKey(headerCompanyApiKey));
		if(Objects.isNull(headerCompanyApiKey) || !apiKeysCacheData.isValidCompanyApiKey(headerCompanyApiKey)) {
			
			SecurityContextHolder.clearContext();
			
			failureHandler.commence(request,
									response, 
									new AuthenticationFailException("api_key invalida o inexistente")
								   );
						
	        return;
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(headerCompanyApiKey, null, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		
		filterChain.doFilter(request, response);
	}
	
	/**
     * Determina si el filtro debe aplicarse a la solicitud actual.
     * 
     * @return true si el filtro NO debe aplicarse, false si debe aplicarse
     */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return pathsToApplyFilter.containsKey(request.getRequestURI()) 
				? !pathsToApplyFilter.get(request.getRequestURI()).contains(request.getMethod()) 
				: true;
	}
}
