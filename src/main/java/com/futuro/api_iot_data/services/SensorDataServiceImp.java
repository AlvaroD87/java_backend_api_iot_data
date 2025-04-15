package com.futuro.api_iot_data.services;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.SensorData;
import com.futuro.api_iot_data.models.DTOs.SensorDataDTO;
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
					.message("sensor api-key invalido")
					.build();
		}
		
		Integer totalData = sensorDataRepo.saveAll(dataList.stream()
															.map(jsonData -> SensorData.builder()
																						.data(jsonData)
																						.sensorId(sensorId)
																						.is_active(true)
																						.createdEpoch(insertInstant)
																						.build()
															).toList()
												   ).size();
		return ResponseServices.builder()
				.code(200)
				.message(String.format("%d datos insertados", totalData))
				.build();
	}

	@Override
	public ResponseServices getData(JsonNode parameters) {
		
		Set<String> querySensorCategory = StreamSupport.stream(parameters.get("sensorCategory").spliterator(), false)
														.map(c -> c.asText())
														.collect(Collectors.toSet());
		
		Set<Integer> querySensorId = parameters.get("sensorId").isEmpty()
												? apiKeysCacheData.getCompanySensorIds(parameters.get("companyApiKey").asText())
												: StreamSupport.stream(parameters.get("sensorId").spliterator(), false)
																.map(i -> i.asInt())
																.collect(Collectors.toSet()); 
		
		querySensorId.retainAll(apiKeysCacheData.getCompanySensorIds(parameters.get("companyApiKey").asText()));
		
		Integer fromEpoch = parameters.get("fromEpoch").canConvertToInt() ? parameters.get("fromEpoch").asInt() : null;
		Integer toEpoch = parameters.get("toEpoch").canConvertToInt() ? parameters.get("toEpoch").asInt() : null;
		
		List<SensorData> queryResult = sensorDataRepo.findAllByParameters(querySensorId, 
																		  fromEpoch, 
																		  toEpoch, 
																		  querySensorCategory);
		
		return ResponseServices.builder()
				.code(200)
				.message(String.format("%d set de datos", queryResult.size()))
				.listModelDTO(queryResult.stream().map(d -> SensorDataDTO.builder().data(d.getData()).build()).toList())
				.build();
	}

}
