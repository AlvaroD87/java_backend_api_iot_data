package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface ICityService {

	ResponseServices create(CityDTO newCity);
	
	ResponseServices getByName(String cityName);
	
	ResponseServices list();
}
