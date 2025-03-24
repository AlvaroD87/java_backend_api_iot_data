package com.futuro.api_iot_data.services;

import java.sql.Date;
import java.util.Calendar;

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
	public ResponseServices create(CityDTO newCity) {
				
		City city = cityRepo.findByCityAndCountry(newCity.getName(), newCity.getCountry().getName()).orElse(null);
		
		if (city == null) {	
			
			return ResponseServices.builder()
					.message("City created")
					.code(200)
					.modelDTO(cityRepo.save(City.builder()
									.name(newCity.getName())
									.country(countryRepo.findByName(newCity.getCountry().getName()).get())
									.is_active(true)
									.created_in(new Date(Calendar.getInstance().getTimeInMillis()))
									.updated_in(new Date(Calendar.getInstance().getTimeInMillis()))
									.build()
								).toCityDTO()
							)
					.build();
			
		}
		
		return ResponseServices.builder()
				.message("City all ready exists")
				.code(300)
				.modelDTO(newCity)
				.build();
	}

	@Override
	public ResponseServices getByName(String cityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseServices listAll() {
		return ResponseServices.builder()
				.listModelDTO(cityRepo.findAll().stream().map(city -> city.toCityDTO()).toList())
				.build();
	}

}
