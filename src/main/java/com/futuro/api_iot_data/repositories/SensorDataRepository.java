package com.futuro.api_iot_data.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.SensorData;

/**
 * Repositorio para la entidad {@link SensorData} que maneja los datos recolectados por sensores.
 * 
 * <p>Proporciona métodos para acceder y consultar datos de sensores con diversos filtros.</p>
 */
@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer>{

	/**
     * Busca datos de sensores aplicando múltiples filtros opcionales.
     * 
     * <p>Consulta nativa que filtra datos de sensores basado en:</p>
     * <ul>
     *   <li>IDs de sensores específicos</li>
     *   <li>Categoría de sensor (opcional)</li>
     *   <li>Rango de fechas en formato epoch (opcional)</li>
     * </ul>
     *
     * @param sensorId Conjunto de IDs de sensores a incluir en la búsqueda (requerido)
     * @param fromEpoch Límite inferior del rango de tiempo (epoch timestamp, opcional)
     * @param toEpoch Límite superior del rango de tiempo (epoch timestamp, opcional)
     * @param sensorCategory Conjunto de categorías de sensor para filtrar (opcional)
     * @return Lista de objetos SensorData que cumplen con los criterios de filtrado
     */
	@Query(value = """
				SELECT
					sd.sensor_data_id,
					sd.data,
					sd.sensor_id,
					sd.is_active,
					sd.created_date
				FROM sensors_data sd JOIN sensors s ON sd.sensor_id = s.sensor_id
			    WHERE s.sensor_id in (:sensorId)
				AND (:sensorCategory IS NULL OR s.sensor_category in (:sensorCategory)) 
			    AND (:fromEpoch IS NULL OR (sd.data->>'datetime')::int >= :fromEpoch) 
			    AND (:toEpoch IS NULL OR (sd.data->>'datetime')::int <= :toEpoch)
			""",
			nativeQuery = true)
	List<SensorData> findAllByParameters(
				@Param("sensorId") Set<Integer> sensorId,
				@Param("fromEpoch") Integer fromEpoch,
				@Param("toEpoch") Integer toEpoch,
				@Param("sensorCategory") Set<String> sensorCategory
			);
}