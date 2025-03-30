package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ServerIPValidator extends OncePerRequestFilter{

	private List<String> pathsToApplyFilter;
	
	public ServerIPValidator(List<String> pathsToApplyFilter) {
		this.pathsToApplyFilter = pathsToApplyFilter;
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
			
			int errorCode = Objects.isNull(serverIp) ? HttpStatus.INTERNAL_SERVER_ERROR.value() : HttpStatus.BAD_REQUEST.value();
			String errorReason = Objects.isNull(serverIp) ? HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() : HttpStatus.BAD_REQUEST.getReasonPhrase();
			String errorMessage = Objects.isNull(serverIp) ? "error interno de servidor" : "cliente no autorizado";
			
			response.setStatus(errorCode);
	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        
	        response.getWriter().write(String.format("{\"status\": %d, \"error\": \"%s\", \"message\": \"%s\"}", errorCode, errorReason, errorMessage));
	        
	        response.getWriter().flush();
	        
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
		String requestPath = request.getRequestURI();
		return !pathsToApplyFilter.stream().anyMatch(p -> requestPath.startsWith(p));
	}
	
	

}
