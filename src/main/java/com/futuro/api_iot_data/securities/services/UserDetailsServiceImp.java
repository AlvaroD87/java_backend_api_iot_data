package com.futuro.api_iot_data.securities.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.repositories.AdminRepository;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Admin admin = adminRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("No existe username %s", username)));
		
		return new User(admin.getUsername(), admin.getPassword(), admin.getIsActive(), true, true, true, Collections.emptyList());
	}

}
