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
				SELECT * 
				FROM sensors_data s 
			    WHERE s.sensor_id in (:sensorId)
			    AND (:fromEpoch IS NULL OR (s.data->>'datetime')::int >= :fromEpoch) 
			    AND (:toEpoch IS NULL OR (s.data->>'datetime')::int <= :toEpoch) 
			""",
			nativeQuery = true)
	List<SensorData> findAllByParameters(
				@Param("sensorId") Set<Integer> sensorId,
				@Param("fromEpoch") Integer fromEpoch,
				@Param("toEpoch") Integer toEpoch
			);
}