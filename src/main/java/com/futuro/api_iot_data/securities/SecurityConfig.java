package com.futuro.api_iot_data.securities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	ApiKeysCacheData apiKeysCacheData;
	
	private final List<String> pathsToValidateByServerIPValidator = List.of("/api/v1/admin/",
																			"/api/v1/city/",
																			"/api/v1/country/"
																			);
	private final List<String> pathsToValidateByCompanyApiKeyValidator = List.of("/api/v1/location/",
																				 "/api/v1/sensor/"
																				);
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
				.csrf(csrf -> csrf.disable())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(http -> {
					http.requestMatchers("/api/v1/sensor-data/**").permitAll();
					http.anyRequest().authenticated();
				})
				.addFilterBefore(new ServerIPValidator(pathsToValidateByServerIPValidator), BasicAuthenticationFilter.class)
				.addFilterBefore(new CompanyApiKeyValidator(apiKeysCacheData, pathsToValidateByCompanyApiKeyValidator), BasicAuthenticationFilter.class)
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
