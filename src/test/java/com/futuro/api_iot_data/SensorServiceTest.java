package com.futuro.api_iot_data;

import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.SensorService;
import com.futuro.api_iot_data.services.util.ResponseServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Pruebas unitarias para el servicio de sensores (SensorService).
 * 
 * <p>Verifica el comportamiento del servicio para operaciones CRUD de sensores,
 * incluyendo creación, consulta, actualización y eliminación.</p>
 */
@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {
	
	@Mock
	private SensorRepository sensorRepository;
	
	@Mock
	private ApiKeysCacheData apiKeyCacheDataMock;

	@Mock
	private Location locationMock;
	
	@Mock
	private LastActionCacheData lastActionCacheData;
	
	@InjectMocks
	private SensorService sensorService;
	
	private Sensor sensor;
	private SensorDTO sensorDTO;
	private LastAction lastActionCreated;
	private LastAction lastActionUpdated;
	private LastAction lastActionDeleted;

	private ObjectMapper mapper = new ObjectMapper();

	/**
     * Configuración inicial para cada prueba.
     */
	@BeforeEach
	void setUp() {
		
		JsonNode jsonMeta;
        try {
        	jsonMeta = mapper.readTree("{\"Prueba\":30}");
		} catch (JsonProcessingException e) {
			jsonMeta = null;
		}
        
		sensor = new Sensor();
		sensor.setSensorId(1);
		sensor.setSensorName("Sensor de Prueba");
		sensor.setSensorCategory("Prueba");
		sensor.setSensorApiKey("abc123");
		sensor.setSensorMeta(jsonMeta);
		sensor.setLocation(locationMock);
		sensor.setIsActive(true);
		sensor.setCreatedOn(LocalDateTime.of(2025,3,2,12,0,0));
		sensor.setUpdatedOn(LocalDateTime.of(2025,3,2,12,0,0));
		
		/*
		sensorDTO = new SensorDTO(
				1,"Sensor de Prueba","Prueba","abc123",
				jsonMeta, locationMock, true,
				Timestamp.valueOf("2025-03-02 12:00:00"),
				Timestamp.valueOf("2025-03-02 13:00:00")
				);
		*/
		
		sensorDTO = SensorDTO.builder()
					.sensorId(1)
					.sensorName("Sensor de Prueba")
					.sensorCategory("Prueba")
					.sensorApiKey("abc123")
					.sensorMeta(jsonMeta)
					.locationId(3)
					.build();
		
		lastActionCreated = new LastAction();
		lastActionCreated.setId(1);
		lastActionCreated.setActionEnum("CREATED");
		
		lastActionUpdated = new LastAction();
		lastActionUpdated.setId(2);
		lastActionUpdated.setActionEnum("UPDATED");
		
		lastActionDeleted = new LastAction();
		lastActionDeleted.setId(3);
		lastActionDeleted.setActionEnum("DELETED");
		
		Authentication authentication = new UsernamePasswordAuthenticationToken("companyApiKey", null, null);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
				
	}
	
	/**
     * Prueba la obtención exitosa de todos los sensores.
     */
	@Test
	void testGetAllSensors() {
		when(sensorRepository.findAllActiveByCompanyApiKey("companyApiKey")).thenReturn(List.of(sensor));
		
		ResponseServices response = sensorService.getAllSensors("companyApiKey");
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Lista de sensores obtenida exitosamente", response.getMessage());		
		assertFalse(response.getListModelDTO().isEmpty());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getListModelDTO().get(0)).getSensorName());
		
	}
	
	/**
     * Prueba la obtención exitosa de un sensor por ID.
     */
	@Test
	void testGetSensorById_Exists() {
		when(sensorRepository.findActiveByIdAndCompanyApiKey("companyApiKey",1)).thenReturn(Optional.of(sensor));
		
		ResponseServices response = sensorService.getSensorById("companyApiKey",1);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor encontrado", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
	}
	
	/**
     * Prueba la creación exitosa de un sensor.
     */
	@Test
	void testCreateSensor() {
		when(sensorRepository.findActiveBySensorNameLocationIdCompanyApiKey("Sensor de Prueba", 3, "companyApiKey"))
			.thenReturn(Optional.empty());
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		when(lastActionCacheData.getLastAction("CREATED"))
			.thenReturn(lastActionCreated);
		
		ResponseServices response = sensorService.createSensor("companyApiKey", sensorDTO);
		
		assertNotNull(response);
		assertEquals(201, response.getCode());
		assertEquals("Sensor creado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
	}
	
	/**
     * Prueba la actualización exitosa de un sensor.
     */
	@Test
	void testUpdateSensor() {
		when(sensorRepository.findActiveByIdAndCompanyApiKey("companyApiKey",1)).thenReturn(Optional.of(sensor));
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		when(lastActionCacheData.getLastAction("UPDATED"))
			.thenReturn(lastActionUpdated);
		
		JsonNode jsonMetaModificado;
        try {
        	jsonMetaModificado = mapper.readTree("{\"Modificado\":50}");
		} catch (JsonProcessingException e) {
			jsonMetaModificado = null;
		}
        
		SensorDTO updateSensor = SensorDTO.builder()
									.sensorId(1)
									.sensorName("Sensor Modificado")
									.sensorCategory("modificación")
									.sensorApiKey("123abcd")
									.sensorMeta(jsonMetaModificado)
									.locationId(3)
									.build();
		
		ResponseServices response = sensorService.updateSensor(1, updateSensor, "companyApiKey");
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor actualizado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor Modificado", ((SensorDTO) response.getModelDTO()).getSensorName());
		assertEquals("modificación", ((SensorDTO) response.getModelDTO()).getSensorCategory());
	}
	
	/**
     * Prueba la eliminación exitosa de un sensor.
     */
	@Test
	void testDeleteSensor() {
		when(sensorRepository.findById(1)).thenReturn(Optional.of(sensor));
		//doNothing().when(sensorRepository).deleteById(1);
		when(sensorRepository.save(any(Sensor.class))).thenReturn(sensor);
		when(lastActionCacheData.getLastAction("DELETED"))
			.thenReturn(lastActionDeleted);
		
		ResponseServices response = sensorService.deleteSensor("companyApiKey",1);
		
		assertNotNull(response);
		assertEquals(200, response.getCode());
		assertEquals("Sensor eliminado exitosamente", response.getMessage());
		assertNotNull(response.getModelDTO());
		assertEquals("Sensor de Prueba", ((SensorDTO) response.getModelDTO()).getSensorName());
		
	}

}
