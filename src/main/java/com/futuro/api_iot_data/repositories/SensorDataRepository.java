package com.futuro.api_iot_data.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer>{

	@Query(value = """
				SELECT
					sd.sensor_data_id,
					sd.data,
					sd.sensor_id,
					sd.is_active,
					sd.created_date
				FROM sensors_data sd JOIN sensors s ON sd.sensor_id = s.sensor_id
			    WHERE (:sensorId IS NULL OR s.sensor_id in (:sensorId))
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