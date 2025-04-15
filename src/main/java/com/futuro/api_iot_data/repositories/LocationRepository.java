package com.futuro.api_iot_data.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Location;

/**
 * Repositorio para la entidad {@link Location}
 * Proporciona métodos para acceder y gestionar datos relacionados con locaciones en la base de datos.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location,Integer>{
	/**
     * Verifica si existe una locación con el nombre especificado.
     *
     * @param LocationName Nombre de la locación a verificar.
     * @return {@code true} si existe una locación con el nombre proporcionado, {@code false} en caso contrario.
     */
	boolean existsByLocationName(String LocationName);

	/**
     * Verifica si existe una locación con el nombre especificado, excluyendo una locación con un ID determinado.
     * Este método es útil para validaciones durante la actualización de una locación, asegurando que no haya
     * duplicados en el nombre de la locación.
     *
     * @param LocationName Nombre de la locación a verificar.
     * @param locationId   ID de la locación que se debe excluir de la verificación.
     * @return {@code true} si existe una locación con el nombre proporcionado (excluyendo la locación con el ID dado),
     *         {@code false} en caso contrario.
     */
	boolean existsByLocationNameAndLocationIdNot(String LocationName, Integer locationId);
	
	/**
     * Obtiene todos los API Keys de sensores asociados a una locación.
     * 
     * @param locationId ID de la locación
     * @return Lista de strings con los API Keys de los sensores
     */
	@Query(value="""
			select 
				s.sensor_api_key 
			from locations l 
				join sensors s on l.location_id = s.location_id 
			where 
				s.location_id = ?1
			""", 
			nativeQuery = true)
	List<String> findAllSensorIdByLocationId(Integer locationId);
	
	/**
     * Actualiza el estado activo de una locación específica.
     * 
     * @param locationId ID de la locación a actualizar
     * @param statusIsActive Nuevo estado activo (true/false)
     */
	@Modifying
	@Query(value = """
			update locations 
			set 
				is_active = ?2 
			where 
				location_id = ?1
			""", 
			nativeQuery = true)
	void updateStatusByLocationId(Integer locationId, boolean statusIsActive);
	
	/**
     * Actualiza el estado de múltiples locaciones pertenecientes a una compañía.
     * 
     * @param companyId ID de la compañía cuyas locaciones se actualizarán
     * @param statusIsActive Nuevo estado activo (true/false)
     * @param lastActionId ID de la última acción registrada
     */
	@Modifying
	@Query(value = """
			update locations 
			set 
				is_active = ?2 ,
				last_action_id = ?3
			where 
				company_id = ?1
			""", 
			nativeQuery = true)
	void updateStatusByCompanyId(Integer companyId, boolean statusIsActive, Integer lastActionId);
	
	/**
     * Obtiene todas las locaciones activas de una compañía.
     * 
     * @param companyApiKey API Key de la compañía
     * @return Lista de locaciones activas
     */
	@Query(value = """
			select l 
			from Location l 
				join l.company c 
			where 
				c.companyApiKey = ?1 
				and l.isActive = True
			"""
			)
	List<Location> findAllActiveByCompanyApiKey(String companyApiKey);
	
	/**
     * Busca una locación activa por su ID y API Key de compañía.
     * 
     * @param id ID de la locación
     * @param companyApyKey API Key de la compañía
     * @return Optional con la locación si existe y está activa, vacío en caso contrario
     */
	@Query(value = """
			select l 
			from Location l 
				join l.company c 
			where 
				l.locationId = ?1 
				and c.companyApiKey = ?2 
				and l.isActive = True
			""")
	Optional<Location> findActiveByIdAndCompanyApiKey(Integer id, String companyApyKey);
}
