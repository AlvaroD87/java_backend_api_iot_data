package com.futuro.api_iot_data.services;

import java.util.List;

import com.futuro.api_iot_data.dtos.LocationDTO;

/**
 * Interfaz para la implementación de un servicio que provea métodos para la gestión de locaciones,
 * tales como crear una nueva locación, buscar una locación por ID,
 * actualizar una locación, eliminar una locación o listar todas las locaciones disponibles.
 */
public interface LocationService {
	/**
	 * Crea una nueva locación
	 * 
	 * @param locationDTO DTO con la información de la locación
	 * @return El DTO de la locación creada
	 */
	public LocationDTO create(LocationDTO locationDTO);


	/**
	 * Actualiza una locación existente
	 * @param id ID de la locación a actualizar
	 * @param locationDTO DTO con la información de la locación
	 * @return El DTO de la locación actualizada
	 */
	public LocationDTO update(Long id, LocationDTO locationDTO);

	/**
	 * Obtiene una lista de todas las locaciones registradas
	 * @return Lista de DTOs de locaciones
	 */
	public List<LocationDTO> findAll();

	/**
	 * Elimina la locación asociada al id especificado
	 * @param id Id de la locación a eliminar
	 * @return El DTO de la locación eliminada
	 */
	public LocationDTO deleteById(Long id);

	/**
	 * Obtiene una locación a partir de su identificador
	 * @param id Id de la locación a buscar
	 * @return El DTO de la locación encontrada
	 */
	public LocationDTO findById(Long id);
	
}
