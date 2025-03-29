package com.futuro.api_iot_data.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.repositories.CompanyRepository;

@Component
public class CompanyCacheData {
	
	CompanyRepository companyRepo;
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> companySensorApiKey = new ConcurrentHashMap<>();

	public CompanyCacheData(CompanyRepository companyRepo) {
		
		this.companyRepo = companyRepo;
		
		this.companyRepo.joinedCompanyKeySensorKey().stream()
			.forEach(r -> {
				String companyApiKey = (String) r[0];
				String sensorApiKey = (String) r[1];
				Integer sensorId = (Integer) r[2];
				
				companySensorApiKey.computeIfAbsent(companyApiKey, s -> new ConcurrentHashMap<String, Integer>())
									.put(sensorApiKey,sensorId);
			});
		
	}
	
	public boolean isValidCompanyApiKey(String companyApiKey) {
		return companySensorApiKey.containsKey(companyApiKey);
	}
	
	public boolean isValidSensorApiKey(String sensorApiKey) {
		return companySensorApiKey.searchValues(1, s -> s.containsKey(sensorApiKey) ? true : null);
	}
	
	public List<Integer> getCompanySensorIds(String companyApiKey) {
		
		return new ArrayList<Integer>(companySensorApiKey.getOrDefault(companyApiKey, 
														new ConcurrentHashMap<String, Integer>()
														)
											.values()
									 );
	}
	
	public Integer getSensorId(String sensorApiKey) {
		return companySensorApiKey.searchValues(1, s -> s.get(sensorApiKey));
	}
}
