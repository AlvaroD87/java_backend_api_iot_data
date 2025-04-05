package com.futuro.api_iot_data.securities;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.securities.util.ServerIPValidator;
import com.futuro.api_iot_data.securities.util.CompanyApiKeyValidator;
import com.futuro.api_iot_data.securities.util.CustomAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	ApiKeysCacheData apiKeysCacheData;
	
	private final Map<String, List<String>> pathsToValidateByServerIp = Map.of("/api/v1/admin", List.of("POST","PUT","DELETE"), 
																			   "/api/v1/city", List.of("POST","PUT","DELETE"), 
																			   "/api/v1/country", List.of("POST","PUT","DELETE")
																			  );
	
	private final Map<String, List<String>> pathsToValidateByApiKey = Map.of("/api/v1/city", List.of("GET"),
																			 "/api/v1/country", List.of("GET"),
																			 "/api/v1/location", List.of("GET","POST","PUT","DELETE"),
																			 "/api/v1/sensor", List.of("GET","POST","PUT","DELETE"),
																			 "/api/v1/sensor_data", List.of("GET")
																			);
	@Autowired
	private CustomAuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(http -> {
					http.requestMatchers(HttpMethod.POST,"/api/v1/sensor_data").permitAll();
					http.anyRequest().authenticated();
				})
				.addFilterBefore(new ServerIPValidator(pathsToValidateByServerIp, authenticationEntryPoint), BasicAuthenticationFilter.class)
				.addFilterBefore(new CompanyApiKeyValidator(apiKeysCacheData, pathsToValidateByApiKey, authenticationEntryPoint), BasicAuthenticationFilter.class)
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
				.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	AuthenticationProvider userPasswordProvider(UserDetailsService userDetailService, PasswordEncoder passwordEncode) {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(passwordEncode);
		return provider;
	}
}
