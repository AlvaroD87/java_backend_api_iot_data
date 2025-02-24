package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DAOs.AdminDAO;
import com.futuro.api_iot_data.services.AdminServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	AdminServiceImp adminService;
	
	@PostMapping("/create")
	public ResponseEntity<AdminDAO> createAdmin(@RequestBody AdminDAO newAdmin){
		
		ResponseServices response = adminService.create(newAdmin);
		
		return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.CONFLICT).body((AdminDAO)response.getModelDAO());
	}
}
