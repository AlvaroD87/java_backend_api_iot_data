package com.futuro.api_iot_data.services;

//import java.sql.Date;
//import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
								.map(country -> country.toCountryDTO())
								.toList()
							 )
				.build();
	}
	
	/*@Override
	public ResponseServices create(CountryDTO newCountry) {
		
		if(countryRepo.findByName(newCountry.getName()).isEmpty()) {
			return ResponseServices.builder()
					.modelDTO(countryRepo.save(Country.builder()
													.name(newCountry.getName())
													.is_active(true)
													.created_in(new Date(Calendar.getInstance().getTimeInMillis()))
													.updated_in(new Date(Calendar.getInstance().getTimeInMillis()))
													.build()
											  ).toCountryDTO()
							 )
					.message("Country created")
					.code(200)
					.build();
		}
		
		return ResponseServices.builder()
				.modelDTO(newCountry)
				.message("Country already exists")
				.code(300)
				.build();
	}*/

	/*@Override
	public ResponseServices getByName(String countryName) {
		
		Country country = countryRepo.findByName(countryName).orElse(null);
		
		return ResponseServices.builder()
				.modelDTO(country == null ? new CountryDTO() : new CountryDTO(country))
				.message(country == null ? "Country doesn't exist" : "Country finded")
				.code(country == null ? 300 : 200)
				.build();
	}*/

}
