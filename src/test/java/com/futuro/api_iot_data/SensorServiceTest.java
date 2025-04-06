package com.futuro.api_iot_data;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.SensorService;
import com.futuro.api_iot_data.services.util.ResponseServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {
	
	@Mock
	private SensorRepository sensorRepository;
	@Mock
	private ApiKeysCacheData apiKeyCacheDataMock;
	
	
	@InjectMocks
	private SensorService sensorService;
	
	private Sensor sensor;
	private SensorDTO sensorDTO;
	
	@BeforeEach
	void setUp() {
		sensor = new Sensor();
		sensor.setSensorId(1);
		sensor.setSensorName("Sensor de Prueba");
		sensor.setSensorCategory("Prueba");
		sensor.setSensorApiKey("abc123");
		sensor.setSensorMeta(Map.of("Prueba", 30));
		sensor.setLocationId(3);
		sensor.setIsActive(true);
		sensor.setCreatedDate(Timestamp.valueOf("2025-03-02 12:00:00"));
		sensor.setUpdateDate(Timestamp.valueOf("2025-03-02 13:00:00"));
		
		sensorDTO = new SensorDTO(
				1,"Sensor de Prueba","Prueba","abc123",
				Map.of("Prueba", 30), 3, true,
				Timestamp.valueOf("2025-03-02 12:00:00"),
				Timestamp.valueOf("2025-03-02 13:00:00")
				);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken("companyApiKey", null, null);
		SecurityContext context = SecurityContextHolder.getContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
				
	}
	
	@Test
	void testGetAllSensors() {
		when(sensorRepository.findAll()).thenReturn(List.of(sensor));
		
		ResponseServices response = sensorService.getAllSensors();
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Lista de sensores obtenida exitosamente", response.getMessage());		
		assertFalse(response.getListModelDTO().isEmpty());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getListModelDTO().get(0)).getSensorName());
		
	}
	
	@Test
	void testGetSensorById_Exists() {
		when(sensorRepository.findById(1)).thenReturn(Optional.of(sensor));
		
		ResponseServices response = sensorService.getSensorById(1);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor encontrado", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
	}
	
	@Test
	void testCreateSensor() {
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		
		ResponseServices response = sensorService.createSensor(sensorDTO);
		
		assertNotNull(response);
		assertEquals(201, response.getCode());
		assertEquals("Sensor creado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
	}
	
	@Test
	void testUpdateSensor() {
		when(sensorRepository.findById(1)).thenReturn(Optional.of(sensor));
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		
		SensorDTO updateSensor = new SensorDTO(
				1, "Sensor Modificado", "modificación", "123abcd",
				Map.of("Modificado", 50), 3, false,
				Timestamp.valueOf("2025-03-03 12:00:00"),
				Timestamp.valueOf("2025-03-03 13:00:00")
				);
		
		ResponseServices response = sensorService.updateSensor(1, updateSensor);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor actualizado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor Modificado", ((SensorDTO) response.getModelDTO()).getSensorName());
		assertEquals("modificación", ((SensorDTO) response.getModelDTO()).getSensorCategory());
	}
	
	@Test
	void testDeleteSensor() {
		when(sensorRepository.findById(1)).thenReturn(Optional.of(sensor));
		doNothing().when(sensorRepository).deleteById(1);
		
		ResponseServices response = sensorService.deleteSensor(1);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor eliminado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
		
	}

}
