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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.futuro.api_iot_data.services.SensorDataServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@RestController
@RequestMapping("/api/v1/sensor-data")
public class SensorDataController {

	@Autowired
	SensorDataServiceImp sensorDataService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@PostMapping("/insert-data")
	public ResponseEntity<ResponseServices> insertData(@RequestBody JsonNode jsonBody) {
		
		JsonNode jsonApiKey = jsonBody.get("api_key");
		JsonNode jsonData = jsonBody.get("json_data");
		
		if(jsonData == null || jsonApiKey == null ) {
			return ResponseEntity
					.status(HttpStatus.OK) // TO-DO Modificar por mejor código de status
					.body(ResponseServices.builder()
							.code(300)
							.message("El body de la solicitud inválido")
					.build());
		}
		
		List<JsonNode> dataList = jsonData.isArray() ? StreamSupport.stream(jsonData.spliterator(), false).collect(Collectors.toList()) : List.of(jsonData);
		String sensorApiKey = jsonApiKey.asText();
		
		ResponseServices response = sensorDataService.insertData(sensorApiKey, dataList);
		
		return ResponseEntity
				.status(response.getCode() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
				.body(response);
	}
	
	@GetMapping("/get-data")
	public ResponseEntity<ResponseServices> getData(
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@RequestParam(name = "from", required = false) Integer fromEpoch,
			@RequestParam(name = "to", required = false) Integer toEpoch,
			@RequestParam(name = "sensor_id", required = false, defaultValue = "") List<Integer> sensorId,
			@RequestParam(name = "sensor_category", required = false, defaultValue = "") List<String> sensorCategory
			)
	{
		ObjectNode parameters = objectMapper.createObjectNode();
		
		parameters.put("companyApiKey", companyApiKey);
		parameters.put("fromEpoch", fromEpoch);
		parameters.put("toEpoch", toEpoch);
		
		ArrayNode arraySensorId = objectMapper.createArrayNode();
		sensorId.stream().filter(i -> i != null).forEach(s -> arraySensorId.add(s));
		parameters.set("sensorId", arraySensorId);
		
		ArrayNode arraySensorCategory = objectMapper.createArrayNode();
		sensorCategory.stream().filter(s -> !s.isBlank()).forEach(c -> arraySensorCategory.add(c));
		parameters.set("sensorCategory", arraySensorCategory);
		
		return ResponseEntity.status(HttpStatus.OK).body(sensorDataService.getData(parameters));
	}
}
