package com.futuro.api_iot_data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer>{

}