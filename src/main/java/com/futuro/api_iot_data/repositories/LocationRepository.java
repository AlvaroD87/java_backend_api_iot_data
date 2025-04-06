package com.futuro.api_iot_data.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
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
	
	@Query(value="select s.sensor_api_key from locations l join sensors s on l.location_id = s.location_id where s.location_id = ?1", nativeQuery = true)
	List<String> findAllSensorIdByLocationId(Integer locationId);
}
