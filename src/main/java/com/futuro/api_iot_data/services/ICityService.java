package com.futuro.api_iot_data.services;

//import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define el contrato para los servicios relacionados con ciudades.
 * 
 * <p>Establece las operaciones básicas para la gestión de ciudades en el sistema,
 * utilizando respuestas estandarizadas.</p>
 */
public interface ICityService {

	/**
     * Obtiene todas las ciudades registradas en el sistema.
     * 
     * @return ResponseServices que contiene:
     *         - Lista de todas las ciudades convertidas a DTOs
     *         - Código de estado HTTP 200 (éxito)
     *         - Mensaje descriptivo del resultado
     */
	ResponseServices listAll();
	
	//ResponseServices create(CityDTO newCity);
	
	//ResponseServices getByName(String cityName);
	
}
