package com.futuro.api_iot_data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.Country;
import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.services.CityServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

	@Mock
	private CityRepository cityRepo;
	
	@Mock
	private CountryRepository countryRepo;
	
	@InjectMocks
	private CityServiceImp cityService;
	
	private City city;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		Country country = Country.builder()
				.id(1)
				.name("Chile")
				.is_active(true)
				.created_in(new Date(System.currentTimeMillis()))
				.updated_in(new Date(System.currentTimeMillis()))
				.build();
		
		city = City.builder()
				.id(1)
				.name("Santiago")
				.country(country)
				.is_active(true)
				.created_in(new Date(System.currentTimeMillis()))
				.updated_in(new Date(System.currentTimeMillis()))
				.build();
		
	}
	
	@Test
	void testListAllCities_Success() {
		when(cityRepo.findAll()).thenReturn(List.of(city));
		
		ResponseServices response = cityService.listAll();
		
		assertNotNull(response);
		assertNotNull(response.getListModelDTO());
		assertEquals(1, response.getListModelDTO().size());
		assertEquals("Santiago", ((CityDTO) response.getListModelDTO().get(0)).getName());
		
		verify(cityRepo, times(1)).findAll();
		
	}
}
