package com.futuro.api_iot_data.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.services.SensorDataServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@RestController
@RequestMapping("/api/v1/sensor-data")
public class SensorDataController {

	@Autowired
	SensorDataServiceImp sensorDataService;
		
	@PostMapping("/insert-data")
	public ResponseEntity<ResponseServices> insertData(@RequestHeader("api-key") String sensorApiKey, @RequestBody JsonNode jsonBody) {
		
		JsonNode jsonData = jsonBody.get("json_data");
		
		if(jsonData == null ) {
			return ResponseEntity
					.status(HttpStatus.OK) // TO-DO Modificar por mejor código de status
					.body(ResponseServices.builder().code(300).message(String.format("Recepción de data vacía para api_key %s", sensorApiKey)).build());
		}
		
		List<JsonNode> dataList = jsonData.isArray() ? StreamSupport.stream(jsonData.spliterator(), false).collect(Collectors.toList()) : List.of(jsonData);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(sensorDataService.insertData(sensorApiKey, dataList));
	}
	
	@GetMapping("/get-data")
	public ResponseEntity<ResponseServices> getData(
			@RequestParam(value = "company_api_key", required = true) String companyApiKey
			)
	{
		return null;
	}
}
