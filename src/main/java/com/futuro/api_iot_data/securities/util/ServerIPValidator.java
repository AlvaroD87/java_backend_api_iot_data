package com.futuro.api_iot_data.securities.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.springframework.http.HttpStatus;
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
		
		if(serverIp == null || (!serverIp.equals(clientIp) && !clientIp.equals("127.0.0.1") && !clientIp.equals("0:0:0:0:0:0:0:1"))) {
			response.sendError(
					serverIp == null ? HttpStatus.INTERNAL_SERVER_ERROR.value() : HttpStatus.BAD_REQUEST.value(), 
					serverIp == null ? "Error de Servidor" : "Bad Request"
			);
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
