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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.dtos.SensorDTO;
import com.futuro.api_iot_data.services.SensorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {
	
	@Autowired
	private SensorService sensorService;
	
	@GetMapping
	public ResponseEntity<List<SensorDTO>> getAllSensors() {
		List<SensorDTO> sensors = sensorService.getAllSensors();
		return ResponseEntity.status(HttpStatus.OK).body(sensors);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SensorDTO> getSensorById(@PathVariable Integer id) {
		SensorDTO sensorDTO = sensorService.getSensorById(id);
		return ResponseEntity.status(HttpStatus.OK.value()).body(sensorDTO);
	}
	
	@PostMapping
	public ResponseEntity<SensorDTO> createSensor(@Valid @RequestBody SensorDTO sensorDTO) {
		SensorDTO savedSensor = sensorService.createSensor(sensorDTO);
		return ResponseEntity.status(HttpStatus.OK.value()).body(savedSensor);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SensorDTO> updateSensor(@PathVariable Integer id, @Valid @RequestBody SensorDTO sensorDTO) {
		SensorDTO updatedSensor = sensorService.updateSensor(id, sensorDTO);
		return ResponseEntity.status(HttpStatus.OK.value()).body(updatedSensor);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteSensor(@PathVariable Integer id) {
		sensorService.deleteSensor(id);
		return ResponseEntity.status(HttpStatus.OK.value()).body("Sensor con ID " + id + " eliminado correctamente.");
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
