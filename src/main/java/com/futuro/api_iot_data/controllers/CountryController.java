package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.services.CountryServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

	@Autowired
	CountryServiceImp countryService;
	
	@PostMapping("/create")
	public ResponseEntity<ResponseServices> create(@RequestBody CountryDTO country){
		
		ResponseServices response = countryService.create(country);
		
		return ResponseEntity
				.status(response.getCode() == 200 ? 201 : 400)
				.body(response);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<ResponseServices> listAll(){
		return ResponseEntity.status(200).body(countryService.listAll());
	}
}
