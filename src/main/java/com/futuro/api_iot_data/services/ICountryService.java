package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface ICountryService {

	ResponseServices create(CountryDTO newCountry);
	
	ResponseServices getByName(String countryName);
	
	ResponseServices listAll();
}
