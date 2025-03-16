package com.futuro.api_iot_data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer>{

}
