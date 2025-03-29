package com.futuro.api_iot_data.services;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.cache.SensorCacheData;
import com.futuro.api_iot_data.models.SensorData;
import com.futuro.api_iot_data.repositories.SensorDataRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;

@Service
public class SensorDataServiceImp implements ISensorDataService{

	@Autowired
	SensorDataRepository sensorDataRepo;
	
	@Autowired
	SensorCacheData sensorCacheData;
	
	@Override
	@Transactional
	public ResponseServices insertData(String sensorApiKey, List<JsonNode> dataList) {
		
		Integer sensorId = sensorCacheData.getSensorId(sensorApiKey);
		Instant insertInstant = Instant.now();
		
		List<SensorData> listInsertedData = sensorDataRepo.saveAll(dataList.stream()
																		.map(jsonData -> SensorData.builder()
																						.data(jsonData)
																						.sensorId(sensorId)
																						.createdEpoch(insertInstant)
																						.build()
																			).toList()
																	);
		return ResponseServices.builder()
				.code(200)
				.message("data insertada")
				.build();
	}

	@Override
	public ResponseServices getData(JsonNode parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public SensorCacheData getSensorCacheData() {
		return sensorCacheData;
	}

}
