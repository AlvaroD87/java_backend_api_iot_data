package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.futuro.api_iot_data.cache.CompanyCacheData;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CompanyApiKeyValidator extends OncePerRequestFilter{

	private CompanyCacheData companyCacheData;
	private List<String> pathsToApplyFilter;
	
	public CompanyApiKeyValidator(CompanyCacheData companyCacheData, List<String> pathsToApplyFilter) {
		this.companyCacheData = companyCacheData;
		this.pathsToApplyFilter = pathsToApplyFilter;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String headerCompanyApiKey = request.getHeader("api-key");
		
		if(headerCompanyApiKey == null || !companyCacheData.isValidCompanyApiKey(headerCompanyApiKey)) {
			response.sendError(HttpStatus.BAD_REQUEST.value(), "api_key invalida o inexistente");
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
		String requestPath = request.getRequestURI();
		return !pathsToApplyFilter.stream().anyMatch(p -> requestPath.startsWith(p));
	}

}
