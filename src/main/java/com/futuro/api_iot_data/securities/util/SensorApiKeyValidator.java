package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futuro.api_iot_data.cache.SensorCacheData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SensorApiKeyValidator extends OncePerRequestFilter{

	private SensorCacheData sensorCacheData;
	private List<String> pathsToApplyFilter;
	
	public SensorApiKeyValidator(SensorCacheData sensorCacheData, List<String> pathsToApplyFilter) {
		this.sensorCacheData = sensorCacheData;
		this.pathsToApplyFilter = pathsToApplyFilter;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String headerSensorApiKey = request.getHeader("api-key");
		
		if(headerSensorApiKey == null || !sensorCacheData.validApiKey(headerSensorApiKey)) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "api_key invalida o inexistente");
			return;
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(headerSensorApiKey, null, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		
		filterChain.doFilter(request, response);
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String requestPath = request.getRequestURI();
		return !pathsToApplyFilter.stream().anyMatch(p -> requestPath.startsWith(p));
	}

}
