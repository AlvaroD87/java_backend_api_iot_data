package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.LocationDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz para la implementación de un servicio que provea métodos para la gestión de locaciones,
 * tales como crear una nueva locación, buscar una locación por ID,
 * actualizar una locación, eliminar una locación o listar todas las locaciones disponibles.
 */
public interface ILocationService {
	/**
	 * Crea una nueva locación
	 * 
	 * @param locationDTO {@link LocationDTO} con la información de la locación
	 * @return {@link ResponseServices} incluyendo la {@link LocationDTO} creada
	 */
	public ResponseServices create(LocationDTO locationDTO);


	/**
	 * Actualiza una locación existente
	 * @param id ID de la locación a actualizar
	 * @param locationDTO {@link LocationDTO} con la información de la locación
	 * @return {@link ResponseServices} incluyendo la {@link LocationDTO} actualizada
	 */
	public ResponseServices update(Integer id, LocationDTO locationDTO);

	/**
	 * Obtiene una lista de todas las locaciones registradas
	 * @return {@link ResponseServices} incluyendo una lista de todas las {@link LocationDTO} encontradas
	 */
	public ResponseServices findAll();

	/**
	 * Elimina la locación asociada al id especificado
	 * @param id Id de la locación a eliminar
	 * @return {@link ResponseServices} incluyendo la {@link LocationDTO} eliminada
	 */
	public ResponseServices deleteById(Integer id);

	/**
	 * Obtiene una locación a partir de su identificador
	 * @param id Id de la locación a buscar
	 * @return {@link ResponseServices} incluyendo la {@link LocationDTO} encontrada
	 */
	public ResponseServices findById(Integer id);
	
}
