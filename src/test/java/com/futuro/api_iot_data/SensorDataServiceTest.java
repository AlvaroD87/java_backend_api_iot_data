package com.futuro.api_iot_data;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

import java.util.HashSet;
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
	private JsonNode validData;
	private JsonNode getDataParams;
	
	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		
		try {
			validData = objectMapper.readTree("{\"temperature\": 25}");
			
			String jsonParams = "{"
					+ "\"companyApiKey\":\"apikey123\","
					+ "\"sensorCategory\":[\"temperature\"],"
					+ "\"sensorId\":[],"
					+ "\"fromEpoch\":700000000,"
					+ "\"toEpoch\":1900000000"
					+ "}";
			
			getDataParams = objectMapper.readTree(jsonParams);
			
		} catch(Exception e) {
			fail("Error al parsear JSON de prueba: " + e.getMessage());
		}

	}
	
	@Test
	void testInsertData_Success() throws Exception {
		List<JsonNode> dataList = List.of(validData);
		when(apiKeysCacheData.getSensorId("apikey123")).thenReturn(1);
		when(sensorDataRepo.saveAll(anyList())).thenAnswer(i -> i.getArgument(0));
		
		ResponseServices response = sensorDataService.insertData("apikey123", dataList);
		
		assertEquals(200, response.getCode());
		assertTrue(response.getMessage().contains("1 datos insertados"));
		
	}
	
	@Test
	void testInsertData_InvalidApiKey() {
		List<JsonNode> dataList = List.of(validData);
		when(apiKeysCacheData.getSensorId("invalid-key")).thenReturn(null);
		
		ResponseServices response = sensorDataService.insertData("invalid-key", dataList);
		
		assertEquals(400, response.getCode());
		assertEquals("sensor api-key invalido", response.getMessage());
	}
	
	@Test
	void testGetData_Success() throws Exception {
		Set<Integer> sensorIds = new HashSet<>(Set.of(1, 2));
		List<SensorData> sensorDataList = List.of(SensorData.builder().data(validData).build());
		
		when(apiKeysCacheData.getCompanySensorIds("apikey123")).thenReturn(sensorIds);
		when(sensorDataRepo.findAllByParameters(anySet(), any(), any(), anySet())).thenReturn(sensorDataList);
		
		ResponseServices response = sensorDataService.getData(getDataParams);
		
		assertEquals(200, response.getCode());
		assertTrue(response.getMessage().contains("1 set de datos"));
		assertNotNull(response.getListModelDTO());
		
	}
	

}
