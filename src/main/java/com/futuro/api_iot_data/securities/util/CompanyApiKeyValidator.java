package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CompanyApiKeyValidator extends OncePerRequestFilter{

	private ApiKeysCacheData apiKeysCacheData;
	private Map<String,List<String>> pathsToApplyFilter;
	
	public CompanyApiKeyValidator(ApiKeysCacheData apiKeysCacheData, Map<String,List<String>> pathsToApplyFilter) {
		this.apiKeysCacheData = apiKeysCacheData;
		this.pathsToApplyFilter = pathsToApplyFilter;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String headerCompanyApiKey = request.getHeader("api-key");
		System.out.println(apiKeysCacheData.isValidCompanyApiKey(headerCompanyApiKey));
		if(Objects.isNull(headerCompanyApiKey) || !apiKeysCacheData.isValidCompanyApiKey(headerCompanyApiKey)) {
			
			SecurityContextHolder.clearContext();
			
			response.setStatus(HttpStatus.BAD_REQUEST.value());
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        
	        response.getWriter().write(String.format("{\"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}", 
	        											HttpStatus.BAD_REQUEST.value(), 
	        											HttpStatus.BAD_REQUEST.getReasonPhrase(), 
	        											"api_key invalida o inexistente"
	        										)
	        							);
	        
	        response.getWriter().flush();
	        
	        return;
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(headerCompanyApiKey, null, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return pathsToApplyFilter.containsKey(request.getRequestURI()) 
				? !pathsToApplyFilter.get(request.getRequestURI()).contains(request.getMethod()) 
				: true;
	}
}
