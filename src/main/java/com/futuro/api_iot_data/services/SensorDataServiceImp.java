package com.futuro.api_iot_data.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.SensorData;
import com.futuro.api_iot_data.repositories.SensorDataRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;

@Service
public class SensorDataServiceImp implements ISensorDataService{

	@Autowired
	SensorDataRepository sensorDataRepo;
	
	@Autowired
	ApiKeysCacheData apiKeysCacheData;
	
	@Override
	@Transactional
	public ResponseServices insertData(String sensorApiKey, List<JsonNode> dataList) {
		
		Integer sensorId = apiKeysCacheData.getSensorId(sensorApiKey);
		Instant insertInstant = Instant.now();
		
		if (sensorId == null) {
			return ResponseServices.builder()
					.code(400)
					.message("sensor api-key invilado")
					.build();
		}
		
		Integer totalData = sensorDataRepo.saveAll(dataList.stream()
															.map(jsonData -> SensorData.builder()
																						.data(jsonData)
																						.sensorId(sensorId)
																						.createdEpoch(insertInstant)
																						.build()
															).toList()
												   ).size();
		return ResponseServices.builder()
				.code(200)
				.message(String.format("%d data insertada", totalData))
				.build();
	}

	@Override
	public ResponseServices getData(JsonNode parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
