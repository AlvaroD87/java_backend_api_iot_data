package com.futuro.api_iot_data.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.repositories.LastActionRepository;

@Component
public class LastActionCacheData {

	private LastActionRepository lastActionRepo;
	
	private ConcurrentHashMap<String, LastAction> allLastAction = new ConcurrentHashMap<>();
	
	public LastActionCacheData(LastActionRepository lastActionRepo) {
		this.lastActionRepo = lastActionRepo;
		
		this.lastActionRepo.findAll().stream()
		.forEach(l -> {
			allLastAction.put(l.getActionEnum(), l);
		});
	}
	
	public LastAction getLastAction(String actionEnum) {
		return allLastAction.get(actionEnum);
	}
}
