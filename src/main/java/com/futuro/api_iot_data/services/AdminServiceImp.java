package com.futuro.api_iot_data.services;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.DAOs.AdminDAO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.securities.services.PasswordEncoderImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class AdminServiceImp implements IAdminService{

	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	PasswordEncoderImp passwordService;
	
	@Override
	public ResponseServices create(AdminDAO newAdminDAO) {
		
		if(adminRepo.findByUsername(newAdminDAO.getUsername()).isPresent()) {
			return ResponseServices.builder()
					.modelDAO(newAdminDAO)
					.message("Duplicate username")
					.code(300)
					.build();
		};
		
		adminRepo.save(Admin.builder()
				.username(newAdminDAO.getUsername())
				.password(passwordService.encode(newAdminDAO.getPassword()))
				.is_active(true)
				.created_in(new Date(Calendar.getInstance().getTimeInMillis()))
				.updated_in(new Date(Calendar.getInstance().getTimeInMillis()))
				.build()
		);
		
		return ResponseServices.builder()
				.modelDAO(newAdminDAO)
				.message("Admin created")
				.code(200)
				.build();
	}
	
}
