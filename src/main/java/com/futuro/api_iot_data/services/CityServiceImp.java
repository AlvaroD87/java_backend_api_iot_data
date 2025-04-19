package com.futuro.api_iot_data.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Implementación del servicio para operaciones relacionadas con ciudades.
 * 
 * <p>Gestiona la lógica de negocio para el manejo de ciudades incluyendo
 * creación, consulta y listado de ciudades.</p>
 */
@Service
public class CityServiceImp implements ICityService{

	@Autowired
	CityRepository cityRepo;
	
	@Autowired
	CountryRepository countryRepo;
	
	/**
     * Obtiene todas las ciudades de la base de datos y las convierte a DTOs.
     * 
     * @return ResponseServices que contiene:
     *         - Lista de objetos CityDTO
     *         - Estado HTTP 200 (implícito)
     */
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
		return CityDTO.builder().name(city.getName()).id(city.getId()).build();
	}

}
