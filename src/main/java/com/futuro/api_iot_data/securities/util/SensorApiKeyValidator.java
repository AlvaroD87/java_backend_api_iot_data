package com.futuro.api_iot_data.securities.util;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futuro.api_iot_data.util.SensorCacheData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SensorApiKeyValidator extends OncePerRequestFilter{

	private SensorCacheData sensorCacheData;
	private String pathToApplyFilter;
	
	public SensorApiKeyValidator(SensorCacheData sensorCacheData, String pathToApplyFilter) {
	//public SensorApiKeyValidator(SensorCacheData sensorCacheData) {
		this.sensorCacheData = sensorCacheData;
		this.pathToApplyFilter = pathToApplyFilter;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//System.out.println("-- INICIO DE FILTRO SENSOR.API.KEY.VALIDATOR ---");
		
		String headerSensorApiKey = request.getHeader("api-key");
		//System.out.println("-- VALOR DE API-KEY EN HEADER " + headerSensorApiKey + " ---");
		
		//System.out.println("-- VALORES DE SENSOR.CACHE.DATA " + sensorCacheData.getValues() + " ---");
		
		if(headerSensorApiKey == null || !sensorCacheData.validApiKey(headerSensorApiKey)) {
			//System.out.println("-- VALOR DE API-KEY NO VÁLIDO ---\n");
			response.sendError(HttpStatus.BAD_REQUEST.value(), "api_key invalida o inexistente");
			return;
		}
		
		//System.out.println("-- VALOR DE API-KEY VÁLIDO ---");
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(headerSensorApiKey, null, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		
		//System.out.println("-- AUTENTICACIÓN SETEADA ---\n");
		
		filterChain.doFilter(request, response);
		
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getRequestURI().startsWith(pathToApplyFilter);
	}

}
