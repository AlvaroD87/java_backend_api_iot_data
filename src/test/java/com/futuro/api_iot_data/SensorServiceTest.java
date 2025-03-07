package com.futuro.api_iot_data;

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

import com.futuro.api_iot_data.dtos.SensorDTO;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.SensorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {
	
	@Mock
	private SensorRepository sensorRepository;
	
	@InjectMocks
	private SensorService sensorService;
	
	private Sensor sensor;
	private SensorDTO sensorDTO;
	
	@BeforeEach
	void setUp() {
		sensor = new Sensor();
		sensor.setSensorId(1);
		sensor.setSensorName("Sensor de Test");
		sensor.setSensorCategory("Test");
		sensor.setSensorApiKey("abc123");
		sensor.setSensorMeta(Map.of("Test", 30));
		sensor.setLocationId(3);
		sensor.setIsActive(true);
		sensor.setCreatedDate(Timestamp.valueOf("2025-03-02 12:00:00"));
		sensor.setUpdateDate(Timestamp.valueOf("2025-03-02 13:00:00"));
		
		sensorDTO = new SensorDTO(
				1,"Sensor de Test","Test","abc123",
				Map.of("Test", 30), 3, true,
				Timestamp.valueOf("2025-03-02 12:00:00"),
				Timestamp.valueOf("2025-03-02 13:00:00")
				);
				
	}
	
	@Test
	void testGetAllSensors() {
		when(sensorRepository.findAll()).thenReturn(List.of(sensor));
		
		List<SensorDTO> sensors = sensorService.getAllSensors();
		
		assertFalse(sensors.isEmpty());
		assertEquals(1, sensors.size());
		assertEquals("Sensor de Test", sensors.get(0).getSensorName());
		
	}
	
	@Test
	void testGetSensorById_Exists() {
		when(sensorRepository.findById(1)).thenReturn(Optional.of(sensor));
		
		SensorDTO result = sensorService.getSensorById(1);
		
		assertNotNull(result);
		assertEquals("Sensor de Test", result.getSensorName());
	}
	
	@Test
	void testCreateSensor() {
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		
		SensorDTO result = sensorService.createSensor(sensorDTO);
		
		assertNotNull(result);
		assertEquals("Sensor de Test", result.getSensorName());
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
		
		SensorDTO result = sensorService.updateSensor(1, updateSensor);
		
		assertNotNull(result);
		assertEquals("Sensor Modificado", result.getSensorName());
		assertEquals("Modificado", result.getSensorCategory());
	}
	
	@Test
	void testDeleteSensor() {
		
	}

}
