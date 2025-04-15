package com.futuro.api_iot_data.services;

//import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define los servicios disponibles para la gestión de países.
 * 
 * <p>Establece el contrato para las operaciones de consulta y gestión
 * de la entidad Country, utilizando respuestas estandarizadas.</p>
 */
public interface ICountryService {

	 /**
     * Obtiene el listado completo de países registrados en el sistema.
     * 
     * @return ResponseServices con:
     *         - Código 200 (OK) y lista de países en formato DTO
     *         - Código 204 (NO_CONTENT) si no hay países registrados
     *         - Estructura estandarizada de respuesta
     */
	ResponseServices listAll();
	
	//ResponseServices create(CountryDTO newCountry);
	
	//ResponseServices getByName(String countryName);
	
}
