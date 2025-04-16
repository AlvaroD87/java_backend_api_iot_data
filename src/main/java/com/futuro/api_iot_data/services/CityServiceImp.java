package com.futuro.api_iot_data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

@Service
public class CityServiceImp implements ICityService{

	@Autowired
	CityRepository cityRepo;
	
	@Autowired
	CountryRepository countryRepo;
	
	@Override
	public ResponseServices listAll() {
		return ResponseServices.builder()
				.listModelDTO(cityRepo.findAll().stream()
												.map(city -> cityEntityToDTO(city))
												.toList()
							 )
				.build();
	}
	
	private CityDTO cityEntityToDTO(City city) {
		return CityDTO.builder().name(city.getName()).build();
	}

}
