package com.futuro.api_iot_data.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.util.EntityChangeStatusEvent;
import com.futuro.api_iot_data.services.util.EntityModel;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;

/**
 * Servicio para la gestión de sensores IoT.
 * 
 * <p>Proporciona operaciones CRUD completas para sensores,
 * incluyendo manejo de API Keys y sincronización con el caché.</p>
 */
@Service
public class SensorService implements ISensorService{

	@Autowired
	private SensorRepository sensorRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private ApiKeysCacheData apiKeyCacheData;
	
	@Autowired
	private LastActionCacheData lastActionCacheData;
	
	/**
     * Obtiene todos los sensores activos de una compañía.
     * 
     * @param companyApiKey API Key de la compañía
     * @return ResponseServices con lista de sensores o mensaje de error
     */
	public ResponseServices getAllSensors(String companyApiKey) {
		
		List<Sensor> sensors = sensorRepository.findAllActiveByCompanyApiKey(companyApiKey);
		List<SensorDTO> sensorList = new ArrayList<>();
		
		for(Sensor sensor: sensors) {		
			SensorDTO dto = entityDataToDTO(sensor);
			sensorList.add(dto);		
		}
		
		if(sensorList.isEmpty()) {	
			return ResponseServices.builder()
					.message("No se encontraron sensores")
					.code(404)
					.listModelDTO(new ArrayList<>())
					.build();
		}
		
		return ResponseServices.builder()
				.message("Lista de sensores obtenida exitosamente")
				.code(200)
				.listModelDTO(sensorList)
				.build();
		
	}
	
	/**
     * Obtiene un sensor específico por ID y API Key de compañía.
     * 
     * @param companyApiKey API Key de validación
     * @param sensorId ID del sensor
     * @return ResponseServices con el sensor o mensaje de error
     */
	public ResponseServices getSensorById(String companyApiKey, Integer sensorId) {
		
		Optional<Sensor> sensorOptional = sensorRepository.findActiveByIdAndCompanyApiKey(companyApiKey, sensorId);
		
		if(sensorOptional.isPresent()) {
			Sensor sensor = sensorOptional.get();
			return ResponseServices.builder()
					.message("Sensor encontrado")
					.code(200)
					.modelDTO(entityDataToDTO(sensor))
					.build();
		}
		
		return ResponseServices.builder()
				.message("Sensor con ID " + sensorId + " no encontrado")
				.code(300)
				.modelDTO(new SensorDTO())
				.build();
		
	}
	
	/**
     * Crea un nuevo sensor con API Key generada automáticamente.
     * 
     * @param companyApiKey API Key de la compañía
     * @param sensorDTO Datos del nuevo sensor
     * @return ResponseServices con el sensor creado o mensaje de error
     */
	public ResponseServices createSensor(String companyApiKey, SensorDTO sensorDTO) {
		
		Optional<Sensor> existSensor = sensorRepository.findActiveBySensorNameLocationIdCompanyApiKey(sensorDTO.getSensorName(), sensorDTO.getLocationId(), companyApiKey);
		
		if(existSensor.isPresent()) {
			return ResponseServices.builder()
					.message("El sensor ya existe")
					.code(300)
					.modelDTO(existSensor.map(this::entityDataToDTO).orElse(new SensorDTO()))
					.build();
		}
		
		Sensor sensor = new Sensor();
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		sensor.setSensorApiKey(UUID.randomUUID().toString());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocation(locationRepository.findActiveByIdAndCompanyApiKey(sensorDTO.getLocationId(), companyApiKey).get());
		sensor.setIsActive(true);
		sensor.setCreatedOn(LocalDateTime.now());
		sensor.setUpdatedOn(LocalDateTime.now());
		sensor.setLastAction(lastActionCacheData.getLastAction("CREATED"));
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		apiKeyCacheData.setNewSensorApiKey(companyApiKey, savedSensor.getSensorApiKey(), savedSensor.getSensorId());
		
		return ResponseServices.builder()
				.message("Sensor creado exitosamente")
				.code(201)
				.modelDTO(entityDataToDTO(savedSensor))
				.build();

	}
	
	/**
     * Actualiza un sensor existente.
     * 
     * @param sensorId ID del sensor
     * @param sensorDTO Nuevos datos del sensor
     * @param companyApiKey API Key de validación
     * @return ResponseServices con el sensor actualizado o mensaje de error
     */
	public ResponseServices updateSensor(Integer sensorId, SensorDTO sensorDTO, String companyApiKey) {
		
		Optional<Sensor> optionalSensor = sensorRepository.findActiveByIdAndCompanyApiKey(companyApiKey, sensorId);
		
		if(optionalSensor.isEmpty()) {
			return ResponseServices.builder()
					.message("Sensor con ID " + sensorId + " no encontrado")
					.code(404)
					.modelDTO(new SensorDTO())
					.build();
		}
		
		Sensor sensor = optionalSensor.get();
		
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocation(locationRepository.findActiveByIdAndCompanyApiKey(sensorDTO.getLocationId(), companyApiKey).get());
		sensor.setUpdatedOn(LocalDateTime.now());
		sensor.setLastAction(lastActionCacheData.getLastAction("UPDATED"));
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		return ResponseServices.builder()
				.message("Sensor actualizado exitosamente")
				.code(200)
				.modelDTO(entityDataToDTO(savedSensor))
				.build();
	}
	
	/**
     * Elimina lógicamente un sensor (marca como inactivo).
     * 
     * @param id ID del sensor
     * @return ResponseServices con confirmación o mensaje de error
     */
	@Transactional
	public ResponseServices deleteSensor(String companyApiKey, Integer id) {
		Optional<Sensor> optionalSendor = sensorRepository.findById(id);
		
		if(optionalSendor.isEmpty()) {			
			return ResponseServices.builder()
					.message("Sensor con ID " + id + " no encontrado")
					.code(404)
					.build();
		}
		
		Sensor sensorInfo = optionalSendor.get();
		SensorDTO selectSensor = entityDataToDTO(sensorInfo);
				
		sensorInfo.setIsActive(false);
		sensorInfo.setUpdatedOn(LocalDateTime.now());
		sensorInfo.setLastAction(lastActionCacheData.getLastAction("DELETED"));
		
		sensorRepository.save(sensorInfo);
		
		apiKeyCacheData.deleteSensorApiKey(companyApiKey, sensorInfo.getSensorApiKey());
		
		return ResponseServices.builder()
				.message("Sensor eliminado exitosamente")
				.code(200)
				.modelDTO(selectSensor)
				.build();
	}
	
	 /**
     * Maneja eventos de cambio de estado para actualizar sensores relacionados.
     */
	@EventListener
	@Transactional
	void handlerEventEntityChangeStatus(EntityChangeStatusEvent event) {
		switch (event.getEntity()) {
		case EntityModel.COMPANY: {
			sensorRepository.updateIsActiveByCompanyId(event.getEntityId(), event.isStatus(), event.getLastAction().getId());
			break;
		}
		case EntityModel.LOCATION: {
			sensorRepository.updateIsActiveByLocationId(event.getEntityId(), event.isStatus(), event.getLastAction().getId());
			break;
		}}
	}

	@Override
	public SensorDTO entityDataToDTO(Sensor sensor) {
		return SensorDTO.builder()
				.sensorId(sensor.getSensorId())
				.sensorName(sensor.getSensorName())
				.sensorApiKey(sensor.getSensorApiKey())
				.sensorCategory(sensor.getSensorCategory())
				.sensorMeta(sensor.getSensorMeta())
				.locationId(sensor.getLocation().getLocationId())
				.build();
	}
}
