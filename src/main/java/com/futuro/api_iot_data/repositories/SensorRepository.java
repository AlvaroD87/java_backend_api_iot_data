package com.futuro.api_iot_data.repositories;

import java.util.List;
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
	
	@Query(value = """
			select 
				s 
			from 
				Sensor s 
				join s.location l 
				join l.company c 
			where 
				s.sensorName = ?1 
				and l.locationId = ?2 
				and c.companyApiKey = ?3 
				and s.isActive = True
			""")
	Optional<Sensor> findActiveBySensorNameLocationIdCompanyApiKey(String sensorName, Integer locationId, String companyApiKey);
}
