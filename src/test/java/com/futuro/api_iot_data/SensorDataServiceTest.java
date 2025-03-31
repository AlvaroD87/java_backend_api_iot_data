package com.futuro.api_iot_data;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.SensorData;
import com.futuro.api_iot_data.repositories.SensorDataRepository;
import com.futuro.api_iot_data.services.SensorDataServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
public class SensorDataServiceTest {

	@Mock
	private SensorDataRepository sensorDataRepo;
	
	@Mock
	private ApiKeysCacheData apiKeysCacheData;
	
	@InjectMocks
	private SensorDataServiceImp sensorDataService;
	
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
	}
	
	@Test
	void testInsertData_Success() throws Exception {
		String apiKey = "apikey123";
		Integer sensorId = 1;
		JsonNode jsonData = objectMapper.readTree("{\"temperature\": 25}");
		List<JsonNode> dataList = List.of(jsonData);
		
		when(apiKeysCacheData.getSensorId(apiKey)).thenReturn(sensorId);
		when(sensorDataRepo.saveAll(anyList())).thenReturn(List.of(
					SensorData.builder()
						.sensorId(sensorId)
						.data(jsonData)
						.is_active(true)
						.createdEpoch(Instant.now())
						.build()
				));
		
		ResponseServices response = sensorDataService.insertData(apiKey, dataList);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertTrue(response.getMessage().contains("1 datos insertados"));
		
	}
	
	@Test
	void testInsertData_InvalidApiKey() {
		String apiKey = "invalido";
		when(apiKeysCacheData.getSensorId(apiKey)).thenReturn(null);
		
		ResponseServices response = sensorDataService.insertData(apiKey, List.of());
		
		assertNotNull(response);
		assertEquals(400, response.getCode());
		assertEquals("sensor api-key invalido", response.getMessage());
	}
	
	@Test
	void testGetData_ValidSensorIds() throws Exception {
		String jsonParams = "{\"companyApiKey\": \"company123\", \"sensorId\": [1], \"fromEpoch\": 17000, \"toEpoch\":15000}";
		JsonNode parameters = objectMapper.readTree(jsonParams);
		
		when(apiKeysCacheData.getCompanySensorIds("company123")).thenReturn(Set.of(1));
		when(sensorDataRepo.findAllByParameters(anySet(), any(), any())).thenReturn(List.of(
				SensorData.builder()
					.sensorId(1)
					.data(objectMapper.readTree("{\"temp\": 22}"))
					.is_active(true)
					.createdEpoch(Instant.now())
					.build()
				));
		
		ResponseServices response = sensorDataService.getData(parameters);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertTrue(response.getMessage().contains("1 set de datos"));
	}
	
	@Test
	void testGetData_InvalidSensorIds() throws Exception {
		String jsonParams = "{\"companyApiKey\": \"company123\", \"sensorId\": [99], \"fromEpoch\": 17000, \"toEpoch\":15000}";
		JsonNode parameters = objectMapper.readTree(jsonParams);
		
		when(apiKeysCacheData.getCompanySensorIds("company123")).thenReturn(Set.of(1, 2));
		
		ResponseServices response = sensorDataService.getData(parameters);
		
		assertNotNull(response);
		assertEquals(400, response.getCode());
		assertTrue(response.getMessage().contains("lista de sensor_id consultada no valida"));
	}
	
}
