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

/**
 * Implementación del servicio para operaciones relacionadas con países.
 * 
 * <p>Proporciona métodos para gestionar la entidad Country, incluyendo
 * operaciones de consulta y listado.</p>
 */
@Service
public class CountryServiceImp implements ICountryService{

	@Autowired
	CountryRepository countryRepo;
	
	 /**
     * Obtiene todos los países registrados en el sistema.
     * 
     * @return ResponseServices con:
     *         - Código 200 (éxito)
     *         - Lista de todos los países convertidos a DTOs
     *         - Mensaje vacío (se puede personalizar según necesidades)
     */
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
