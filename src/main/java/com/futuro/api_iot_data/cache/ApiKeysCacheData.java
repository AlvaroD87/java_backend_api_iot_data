package com.futuro.api_iot_data.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.repositories.CompanyRepository;

@Component
public class ApiKeysCacheData {
	
	CompanyRepository companyRepo;
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> apiKeyList = new ConcurrentHashMap<>();

	public ApiKeysCacheData(CompanyRepository companyRepo) {
		
		this.companyRepo = companyRepo;
		
		this.companyRepo.joinedCompanyKeySensorKey().stream()
			.forEach(r -> {
				String companyApiKey = (String) r[0];
				String sensorApiKey = (String) r[1];
				Integer sensorId = (Integer) r[2];
				
				apiKeyList.computeIfAbsent(companyApiKey, s -> new ConcurrentHashMap<String, Integer>());
				if(sensorApiKey != null && sensorId != null) apiKeyList.get(companyApiKey).put(sensorApiKey,sensorId);
			});
		
	}
	
	public boolean isValidCompanyApiKey(String companyApiKey) {
		return companyApiKey != null ? apiKeyList.containsKey(companyApiKey) : false;
	}
	
	public boolean isValidSensorApiKey(String sensorApiKey) {
		return apiKeyList.searchValues(1, s -> s.containsKey(sensorApiKey) ? true : null);
	}
	
	public Set<Integer> getCompanySensorIds(String companyApiKey) {
		return new HashSet<Integer>(apiKeyList.getOrDefault(companyApiKey, new ConcurrentHashMap<String, Integer>()).values());
	}
	
	public Integer getSensorId(String sensorApiKey) {
		return apiKeyList.searchValues(1, s -> s.get(sensorApiKey));
	}
	
	public void setNewCompanyApiKey(String newApiKey) {
		apiKeyList.putIfAbsent(newApiKey, new ConcurrentHashMap<String, Integer>());
	}
	
	public void setNewSensorApiKey(String companyApiKey, String newSensorApiKey, Integer newSensorId) {
		apiKeyList.get(companyApiKey).put(newSensorApiKey, newSensorId);
	}
	
	public void deleteSensorApiKey(String companyApiKey, String sensorApiKey) {
		apiKeyList.get(companyApiKey).remove(sensorApiKey);
	}
}
