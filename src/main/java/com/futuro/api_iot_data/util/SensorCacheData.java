package com.futuro.api_iot_data.util;

import java.util.Map;
//import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.repositories.SensorRepository;

@Component
public class SensorCacheData {

	//@Autowired
	//private SensorRepository sensorRepo;
	private final Map<String, Integer> apiKeyId = new ConcurrentHashMap<String, Integer>();
	//private final Set<String> listApiKey = ConcurrentHashMap.newKeySet();
	
	public SensorCacheData(@Autowired SensorRepository sensorRepo) {
		
		apiKeyId.putAll(sensorRepo.findAll().stream()
				.collect(Collectors.toMap(Sensor::getSensorApiKey, Sensor::getSensorId, (existing, replacement) -> existing))
				);
		
		//listApiKey.addAll(apiKeyId.keySet());
		
	}
	
	public void putApiKey(String apiKey, Integer id) {
		apiKeyId.put(apiKey, id);
	}
	
	public Integer getSensorId(String apiKey) {
		return apiKeyId.get(apiKey);
	}
	
	public void updateApiKey(String oldApiKey, String newApiKey) {
		
		if(apiKeyId.containsKey(oldApiKey)) {
			apiKeyId.put(newApiKey, apiKeyId.get(oldApiKey));
			apiKeyId.remove(oldApiKey);
		}
		
	}
	
	public boolean validApiKey(String sensorApiKey) {
		return apiKeyId.containsKey(sensorApiKey);
	}
	
	public String getValues() {
		return apiKeyId.entrySet().stream()
				.map(e -> "api_key: " + e.getKey() + " ** id: " + e.getValue())
				.collect(Collectors.joining(" ++ "));
	}
}
