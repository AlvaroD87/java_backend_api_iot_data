package com.futuro.api_iot_data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.repositories.SensorDataRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class SensorDataImp implements ISensorData{

	@Autowired
	SensorDataRepository sensorDataRepo;
	
	@Override
	public ResponseServices insert(JsonNode data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseServices getData(JsonNode parameters) {
		// TODO Auto-generated method stub
		return null;
	}

}
