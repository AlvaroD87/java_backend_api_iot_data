package com.futuro.api_iot_data.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class SensorService {

	@Autowired
	private SensorRepository sensorRepository;
	
	@Autowired
	private ApiKeysCacheData apiKeyCacheData;
	
	public ResponseServices getAllSensors() {
		
		List<Sensor> sensors = sensorRepository.findAll();
		List<SensorDTO> sensorList = new ArrayList<>();
		
		for(Sensor sensor: sensors) {		
			SensorDTO dto = new SensorDTO(
					sensor.getSensorId(),
					sensor.getSensorName(),
					sensor.getSensorCategory(),
					sensor.getSensorApiKey(),
					sensor.getSensorMeta(),
					sensor.getLocationId(),
					sensor.getIsActive(),
					sensor.getCreatedDate(),
					sensor.getUpdateDate()
					);
			
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
	
	public ResponseServices getSensorById(Integer sensorId) {
		Optional<Sensor> sensorOptional = sensorRepository.findById(sensorId);
		
		if(sensorOptional.isPresent()) {
			Sensor sensor = sensorOptional.get();
			return ResponseServices.builder()
					.message("Sensor encontrado")
					.code(200)
					.modelDTO(sensor.toSensorDTO())
					.build();
		}
		
		return ResponseServices.builder()
				.message("Sensor con ID " + sensorId + " no encontrado")
				.code(300)
				.modelDTO(new SensorDTO())
				.build();
		
	}
	
	public ResponseServices createSensor(SensorDTO sensorDTO) {
		
		Optional<Sensor> existSensor = sensorRepository.findBySensorNameAndLocationId(sensorDTO.getSensorName(), sensorDTO.getLocationId());
		
		if(existSensor.isPresent()) {
			return ResponseServices.builder()
					.message("El sensro ya existe")
					.code(300)
					.modelDTO(existSensor.map(Sensor::toSensorDTO).orElse(new SensorDTO()))
					.build();
		}
		
		Sensor sensor = new Sensor();
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		//sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
		sensor.setSensorApiKey(UUID.randomUUID().toString());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocationId(sensorDTO.getLocationId());
		sensor.setIsActive(true);
		sensor.setCreatedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		sensor.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		apiKeyCacheData.setNewSensorApiKey(getCompanyApiKeyFromSecurityContext(), savedSensor.getSensorApiKey(), savedSensor.getSensorId());
		
		return ResponseServices.builder()
				.message("Sensor creado exitosamente")
				.code(201)
				.modelDTO(savedSensor.toSensorDTO())
				.build();

	}
	
	public ResponseServices updateSensor(Integer sensorId, SensorDTO sensorDTO) {
		Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);
		
		if(optionalSensor.isEmpty()) {
			return ResponseServices.builder()
					.message("Sensor con ID " + sensorId + " not encontrado")
					.code(404)
					.modelDTO(new SensorDTO())
					.build();
		}
		
		Sensor sensor = optionalSensor.get();
		
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		//sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocationId(sensorDTO.getLocationId());
		//sensor.setIsActive(sensorDTO.getIsActive());
		//sensor.setCreatedDate(sensorDTO.getCreatedDate());
		sensor.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		return ResponseServices.builder()
				.message("Sensor actualizado exitosamente")
				.code(200)
				.modelDTO(savedSensor.toSensorDTO())
				.build();
	}
	
	public ResponseServices deleteSensor(Integer id) {
		Optional<Sensor> optionalSendor = sensorRepository.findById(id);
		
		if(optionalSendor.isEmpty()) {			
			return ResponseServices.builder()
					.message("Sensor con ID " + id + " no encontrado")
					.code(404)
					.build();
		}
		
		Sensor sensorInfo = optionalSendor.get();
		SensorDTO selectSensor = sensorInfo.toSensorDTO();
		
		sensorRepository.deleteById(id);
		
		apiKeyCacheData.deleteSensorApiKey(getCompanyApiKeyFromSecurityContext(), sensorInfo.getSensorApiKey());
		
		return ResponseServices.builder()
				.message("Sensor eliminado exitosamente")
				.code(200)
				.modelDTO(selectSensor)
				.build();
	}
	
	private String getCompanyApiKeyFromSecurityContext() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
}
