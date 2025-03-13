package com.futuro.api_iot_data.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.repositories.SensorRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class SensorService {

	@Autowired
	private SensorRepository sensorRepository;
	
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
					.message("No sensors found")
					.code(404)
					.listModelDTO(new ArrayList<>())
					.build();
		}
		
		return ResponseServices.builder()
				.message("All sensors success")
				.code(200)
				.listModelDTO(sensorList)
				.build();
		
	}
	
	public ResponseServices getSensorById(Integer sensorId) {
		Optional<Sensor> sensorOptional = sensorRepository.findById(sensorId);
		
		if(sensorOptional.isPresent()) {
			Sensor sensor = sensorOptional.get();
			return ResponseServices.builder()
					.message("Sensor find")
					.code(200)
					.modelDTO(sensor.toSensorDTO())
					.build();
		}
		
		return ResponseServices.builder()
				.message("Sensor with ID " + sensorId + " no found")
				.code(300)
				.modelDTO(new SensorDTO())
				.build();
		
	}
	
	public ResponseServices createSensor(SensorDTO sensorDTO) {
		
		Optional<Sensor> existSensor = sensorRepository.findBySensorNameAndLocationId(sensorDTO.getSensorName(), sensorDTO.getLocationId());
		
		if(existSensor.isPresent()) {
			return ResponseServices.builder()
					.message("Sensor alredy exists")
					.code(300)
					.modelDTO(existSensor.map(Sensor::toSensorDTO).orElse(new SensorDTO()))
					.build();
		}
		
		Sensor sensor = new Sensor();
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocationId(sensorDTO.getLocationId());
		sensor.setIsActive(sensorDTO.getIsActive());
		sensor.setCreatedDate(sensorDTO.getCreatedDate());
		sensor.setUpdateDate(sensorDTO.getUpdateDate());
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		return ResponseServices.builder()
				.message("Sensor created successfully")
				.code(201)
				.modelDTO(savedSensor.toSensorDTO())
				.build();

	}
	
	public ResponseServices updateSensor(Integer sensorId, SensorDTO sensorDTO) {
		Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);
		
		if(optionalSensor.isEmpty()) {
			return ResponseServices.builder()
					.message("Sensor with ID " + sensorId + " not found.")
					.code(404)
					.modelDTO(new SensorDTO())
					.build();
		}
		
		Sensor sensor = optionalSensor.get();
		
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocationId(sensorDTO.getLocationId());
		sensor.setIsActive(sensorDTO.getIsActive());
		sensor.setCreatedDate(sensorDTO.getCreatedDate());
		sensor.setUpdateDate(sensorDTO.getUpdateDate());
		
		Sensor savedSensor = sensorRepository.save(sensor);
		
		return ResponseServices.builder()
				.message("Sensor updated successfully")
				.code(200)
				.modelDTO(savedSensor.toSensorDTO())
				.build();
	}
	
	public ResponseServices deleteSensor(Integer id) {
		Optional<Sensor> optionalSendor = sensorRepository.findById(id);
		
		if(optionalSendor.isEmpty()) {			
			return ResponseServices.builder()
					.message("Sensor with ID " + id + " not found.")
					.code(404)
					.build();
		}
		
		Sensor sensorInfo = optionalSendor.get();
		SensorDTO selectSensor = sensorInfo.toSensorDTO();
		
		sensorRepository.deleteById(id);
		
		return ResponseServices.builder()
				.message("Sensor deleted success")
				.code(200)
				.modelDTO(selectSensor)
				.build();
	}
}
