package com.futuro.api_iot_data;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.futuro.api_iot_data.models.Country;
import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.services.CountryServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {

	@Mock
	private CountryRepository countryRepo;
	
	@InjectMocks
	private CountryServiceImp countryService;
	
	private Country country;
	
	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		country = Country.builder()
				.id(1)
				.name("Chile")
				.build();
	}
	
	@Test
	void testListAllCountries_Success() {
		when(countryRepo.findAll()).thenReturn(List.of(country));
		
		ResponseServices response = countryService.listAll();
		
		assertEquals(200, response.getCode());
		assertNotNull(response.getListModelDTO());
		assertEquals(1, response.getListModelDTO().size());
		
		CountryDTO dto = (CountryDTO) response.getListModelDTO().get(0);
		assertEquals("Chile", dto.getName());
		
		verify(countryRepo, times(1)).findAll();
		
	}
	
	@Test
	void testListAllCountries_Empty() {
		when(countryRepo.findAll()).thenReturn(List.of());
		
		ResponseServices response = countryService.listAll();
		
		assertEquals(200, response.getCode());
		assertNotNull(response.getListModelDTO());
		assertTrue(response.getListModelDTO().isEmpty());
		
		verify(countryRepo, times(1)).findAll();
		
	}
	
}
