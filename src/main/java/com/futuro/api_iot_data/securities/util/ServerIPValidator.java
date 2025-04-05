package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServerIPValidator extends OncePerRequestFilter{

	private Map<String,List<String>> pathsToApplyFilter;
	private AuthenticationEntryPoint failureHandler;
	
	public ServerIPValidator(Map<String,List<String>> pathsToApplyFilter, AuthenticationEntryPoint failureHandler) {
		this.pathsToApplyFilter = pathsToApplyFilter;
		this.failureHandler = failureHandler;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String clientIp = request.getRemoteAddr();
		String serverIp;
		
		try {
			serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException("Failed to get server IP", e);
        }
		
		if(Objects.isNull(serverIp) || !(serverIp.equals(clientIp) || clientIp.equals("127.0.0.1") || clientIp.equals("0:0:0:0:0:0:0:1"))) {
						
			SecurityContextHolder.clearContext();
						
			failureHandler.commence(request,
									response, 
									new AuthenticationFailException("api_key invalida o inexistente")
								   );

	        return;
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(clientIp, null, null);
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
