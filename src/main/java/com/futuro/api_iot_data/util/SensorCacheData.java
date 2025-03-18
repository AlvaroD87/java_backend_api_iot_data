package com.futuro.api_iot_data.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.repositories.SensorRepository;

@Component
public class SensorCacheData {

	@Autowired
	private SensorRepository sensorRepo;
	private final Map<String, Integer> apiKeyId = new ConcurrentHashMap<String, Integer>();
	private final Set<String> listApiKey = new ConcurrentSkipListSet<String>();
	
	public SensorCacheData() {
		
		apiKeyId.putAll(apiKeyId);
		
		//sensorRepo.findAll().stream().collect(Collectors.toMap(Sensor::get, null));
		
		apiKeyId.putAll(apiKeyId);
	}
	
	public void putApiKey(String apiKey, Integer id) {
		apiKeyId.put(apiKey, id);
	}
	
	public Integer getSensorId(String apiKey) {
		return apiKeyId.get(apiKey);
	}

}
