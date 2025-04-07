package com.futuro.api_iot_data.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.services.SensorService;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/sensor")
public class SensorController {
	
	@Autowired
	private SensorService sensorService;
	
	/*
	 *	@RequestHeader(name = "api-key", required = true) String companyApiKey,
	 *	@RequestParam(name = "from", required = false) Integer fromEpoch,
	 */
	
	/*@GetMapping("/all")
	public ResponseEntity<ResponseServices> getAllSensors() {
		ResponseServices response = sensorService.getAllSensors();
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}*/
	
	@GetMapping//("/{id}")
	public ResponseEntity<ResponseServices> getSensorById(
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@RequestParam(name = "id", required = false) Integer id) 
	{
		ResponseServices response = id == null ? sensorService.getAllSensors(companyApiKey) : sensorService.getSensorById(companyApiKey,id);
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@PostMapping//("/create")
	public ResponseEntity<ResponseServices> createSensor(@RequestHeader(name = "api-key", required = true) String companyApiKey, @Valid @RequestBody SensorDTO sensorDTO) 
	{
		ResponseServices response = sensorService.createSensor(companyApiKey, sensorDTO);
		if(response.getCode() == 201) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	@PutMapping//("/{id}")
	public ResponseEntity<ResponseServices> updateSensor(
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@RequestParam(name = "id", required = false) Integer id, 
			@Valid @RequestBody SensorDTO sensorDTO
		) 
	{
		ResponseServices response = sensorService.updateSensor(id, sensorDTO, companyApiKey);
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@DeleteMapping//("/{id}")
	public ResponseEntity<ResponseServices> deleteSensor(@RequestParam(name = "sensor_id", required = true) Integer id) {
		ResponseServices response = sensorService.deleteSensor(id);
		
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.BAD_REQUEST.value());
		
		Map<String, String> errors = new HashMap<>();
		for(FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		
		response.put("errors", errors);
		return ResponseEntity.badRequest().body(response);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleSensorNotFound(IllegalArgumentException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.NOT_FOUND.value());
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	 
}
