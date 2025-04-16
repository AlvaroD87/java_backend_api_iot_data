package com.futuro.api_iot_data.services;

//import java.sql.Date;
//import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.Country;
import com.futuro.api_iot_data.models.DTOs.CountryDTO;
//import com.futuro.api_iot_data.models.Country;
//import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class CountryServiceImp implements ICountryService{

	@Autowired
	CountryRepository countryRepo;
	
	@Override
	public ResponseServices listAll() {
		
		return ResponseServices.builder()
				.code(200)
				.message("")
				.listModelDTO(countryRepo.findAll().stream()
								.map(country -> countryEntityToDTO(country))
								.toList()
							 )
				.build();
	}
	
	private CountryDTO countryEntityToDTO(Country country) {
		return CountryDTO.builder().countryId(country.getId()).name(country.getName()).build();
	}

}
