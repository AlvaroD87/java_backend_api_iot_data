package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.services.CityServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@RestController
@RequestMapping("city")
public class CityController {

	@Autowired
	CityServiceImp cityService;

	@PostMapping("/create")
	public ResponseEntity<ResponseServices> create(@RequestBody CityDTO newCity){
		
		ResponseServices response = cityService.create(newCity);
		
		return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
	}
	
	@GetMapping("/get-all")
	public ResponseEntity<ResponseServices> getAll(){
		return ResponseEntity.ok(cityService.listAll());
	}
}
