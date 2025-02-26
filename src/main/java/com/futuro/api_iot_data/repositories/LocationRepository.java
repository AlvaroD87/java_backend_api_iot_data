package com.futuro.api_iot_data.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long>{
	boolean existsByLocationName(String LocationName);
	boolean existsByLocationNameAndLocationIdNot(String LocationName, Long locationId);
}
