package com.futuro.api_iot_data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.City;

@Repository
public interface CityRepository extends JpaRepository<City, Integer>{

}
