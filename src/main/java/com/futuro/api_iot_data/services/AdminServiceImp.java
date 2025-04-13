package com.futuro.api_iot_data.services;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.securities.services.PasswordEncoderImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class AdminServiceImp implements IAdminService{

	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	PasswordEncoderImp passwordService;
	
	@Autowired
	LastActionCacheData lastActionCacheData;
	
	@Override
	public ResponseServices create(AdminDTO newAdminDAO) {
		
		if(adminRepo.findByUsername(newAdminDAO.getUsername()).isPresent()) {
			return ResponseServices.builder()
					.modelDTO(newAdminDAO)
					.message("Usuario Duplicado")
					.code(300)
					.build();
		};
		
		adminRepo.save(Admin.builder()
				.username(newAdminDAO.getUsername())
				.password(passwordService.encode(newAdminDAO.getPassword()))
				.is_active(true)
				.created_in(new Date(Calendar.getInstance().getTimeInMillis()))
				.updated_in(new Date(Calendar.getInstance().getTimeInMillis()))
				.lastAction(lastActionCacheData.getLastAction("CREATED"))
				.build()
		);
		
		return ResponseServices.builder()
				.modelDTO(newAdminDAO)
				.message("Admin created")
				.code(200)
				.build();
	}
	
}
