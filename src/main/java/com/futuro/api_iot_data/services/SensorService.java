package com.futuro.api_iot_data.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.dtos.SensorDTO;
import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.repositories.SensorRepository;

@Service
public class SensorService {

	@Autowired
	private SensorRepository sensorRepository;
	
	public List<SensorDTO> getAllSensors() {
		
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
		
		return sensorList;
	}
	
	public SensorDTO getSensorById(Integer sensorId) {
		Sensor sensor = sensorRepository.findById(sensorId)
				.orElseThrow(() -> new IllegalArgumentException("Sensor con ID " + sensorId + " no encontrado"));
		
		return new SensorDTO(
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
		
	}
	
	public SensorDTO createSensor(SensorDTO sensorDTO) {
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
		
		SensorDTO savedSensorDTO = new SensorDTO(
				savedSensor.getSensorId(),
				savedSensor.getSensorName(),
				savedSensor.getSensorCategory(),
				savedSensor.getSensorApiKey(),
				savedSensor.getSensorMeta(),
				savedSensor.getLocationId(),
				savedSensor.getIsActive(),
				savedSensor.getCreatedDate(),
				savedSensor.getUpdateDate()
				);
				
		return savedSensorDTO;
	}
	
	public SensorDTO updateSensor(Integer sensorId, SensorDTO sensorDTO) {
		Sensor sensor = sensorRepository.findById(sensorId)
				.orElseThrow(() -> new IllegalArgumentException("Sensor con ID " + sensorId + " no encontrado."));
		
		sensor.setSensorName(sensorDTO.getSensorName());
		sensor.setSensorCategory(sensorDTO.getSensorCategory());
		sensor.setSensorApiKey(sensorDTO.getSensorApiKey());
		sensor.setSensorMeta(sensorDTO.getSensorMeta());
		sensor.setLocationId(sensorDTO.getLocationId());
		sensor.setIsActive(sensorDTO.getIsActive());
		sensor.setCreatedDate(sensorDTO.getCreatedDate());
		sensor.setUpdateDate(sensorDTO.getUpdateDate());
		
		sensorRepository.save(sensor);
		return getSensorById(sensorId);
	}
	
	public void deleteSensor(Integer id) {
		if(sensorRepository.existsById(id)) {
			sensorRepository.deleteById(id);
		} else {
			throw new IllegalArgumentException("El sensor con ID " + id + " no existe.");
		}
	}
}
