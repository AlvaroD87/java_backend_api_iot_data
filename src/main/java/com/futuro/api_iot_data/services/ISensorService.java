package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Sensor;
import com.futuro.api_iot_data.models.DTOs.SensorDTO;

public interface ISensorService {

	public SensorDTO entityDataToDTO(Sensor sensor);
	
}
