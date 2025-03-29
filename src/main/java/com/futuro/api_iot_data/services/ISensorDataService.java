package com.futuro.api_iot_data.services;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface ISensorDataService {
	
	ResponseServices insertData(String sensorApiKey, List<JsonNode> dataList);
	
	ResponseServices getData(JsonNode parameters);
	
}
