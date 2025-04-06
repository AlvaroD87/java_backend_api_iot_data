package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
	
	Optional<Sensor> findBySensorApiKey(String sensorApiKey);
	
	Optional<Sensor> findBySensorNameAndLocationId(String sensorName, Integer locationId);

	@Modifying
	@Query(value = "update sensors set is_active = ?2 where location_id in (select location_id from locations where company_id = ?1)", nativeQuery = true)
	void updateIsActiveByCompanyId(Integer companyId, boolean newIsActive);
	
	@Modifying
	@Query(value = "update sensors set is_active = ?2 where location_id = ?1", nativeQuery = true)
	void updateIsActiveByLocationId(Integer locationId, boolean newIsActive);
	
	@Modifying
	@Query(value = "update sensors set is_active = ?2 where sensor_id = ?1", nativeQuery = true)
	void updateIsActiveBySensorId(Integer sensorId, boolean newIsActive);
}
