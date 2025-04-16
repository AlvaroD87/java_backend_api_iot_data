package com.futuro.api_iot_data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Sensor;

/**
 * Repositorio para la entidad {@link Sensor} que gestiona las operaciones de acceso a datos de sensores.
 * 
 */

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
	
	/**
     * Busca un sensor por su API Key única.
     * 
     * @param sensorApiKey La clave API única del sensor
     * @return Optional que contiene el sensor si existe, vacío si no se encuentra
     */
	Optional<Sensor> findBySensorApiKey(String sensorApiKey);
	
	/**
     * Busca un sensor por nombre y ubicación.
     * 
     * @param sensorName Nombre del sensor
     * @param locationId ID de la ubicación asociada
     * @return Optional con el sensor si existe, vacío si no se encuentra
     */
	Optional<Sensor> findBySensorNameAndLocationId(String sensorName, Integer locationId);

	/**
     * Actualiza el estado activo de todos los sensores de una compañía.
     * 
     * @param companyId ID de la compañía
     * @param newIsActive Nuevo estado activo (true/false)
     * @param lastActionId ID de la última acción registrada
     */
	@Modifying
	@Query(value = """
			update sensors 
			set 
				is_active = ?2,
				last_action_id = ?3  
			where 
				location_id in (select location_id from locations where company_id = ?1)
			""", 
			nativeQuery = true)
	void updateIsActiveByCompanyId(Integer companyId, boolean newIsActive, Integer lastActionId);
	
	/**
     * Actualiza el estado activo de todos los sensores de una ubicación.
     * 
     * @param locationId ID de la ubicación
     * @param newIsActive Nuevo estado activo (true/false)
     * @param lastActionId ID de la última acción registrada
     */
	@Modifying
	@Query(value = """
			update sensors 
			set 
				is_active = ?2,
				last_action_id = ?3 
			where 
				location_id = ?1
			""", 
			nativeQuery = true)
	void updateIsActiveByLocationId(Integer locationId, boolean newIsActive, Integer lastActionId);
	
	/**
     * Actualiza el estado activo de un sensor específico.
     * 
     * @param sensorId ID del sensor a actualizar
     * @param newIsActive Nuevo estado activo (true/false)
     */
	@Modifying
	@Query(value = """
			update sensors 
			set 
				is_active = ?2 
			where 
				sensor_id = ?1
			""", 
			nativeQuery = true)
	void updateIsActiveBySensorId(Integer sensorId, boolean newIsActive);
	
	/**
     * Obtiene todos los sensores activos de una compañía.
     * 
     * @param companyApiKey API Key de la compañía
     * @return Lista de sensores activos de la compañía
     */
	@Query(value = """
			select 
				s.* 
			from 
				sensors s 
				join locations l on s.location_id=l.location_id 
				join companies c on l.company_id=c.company_id 
			where 
				c.company_api_key = ?1 
				and s.is_active = True
			""", 
			nativeQuery = true)
	List<Sensor> findAllActiveByCompanyApiKey(String companyApiKey);
	
	/**
     * Busca un sensor activo por ID dentro de una compañía específica.
     * 
     * @param companyApiKey API Key de la compañía
     * @param id ID del sensor
     * @return Optional con el sensor si existe y está activo, vacío si no
     */
	@Query(value = """
			select 
				s.* 
			from 
				sensors s 
				join locations l on s.location_id=l.location_id 
				join companies c on l.company_id=c.company_id 
			where 
				c.company_api_key = ?1 
				and s.sensor_id = ?2 
				and s.is_active = True
			""", 
			nativeQuery = true)
	Optional<Sensor> findActiveByIdAndCompanyApiKey (String companyApiKey, Integer id);
	
	/**
     * Busca un sensor activo por nombre, ubicación y compañía.
     * 
     * @param sensorName Nombre del sensor
     * @param locationId ID de la ubicación
     * @param companyApiKey API Key de la compañía
     * @return Optional con el sensor si existe y está activo, vacío si no
     */
	@Query(value = """
			select 
				s.* 
			from 
				sensors s 
				join locations l on s.location_id=l.location_id 
				join companies c on l.company_id=c.company_id 
			where 
				s.sensor_name = ?1 
				and s.location_id = ?2 
				and c.company_api_key = ?3 
				and s.is_active = True
			""", 
			nativeQuery = true)
	Optional<Sensor> findActiveBySensorNameLocationIdCompanyApiKey(String sensorName, Integer locationId, String companyApiKey);
}
