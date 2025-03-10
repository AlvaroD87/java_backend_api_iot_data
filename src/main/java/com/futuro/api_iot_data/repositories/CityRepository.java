package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.City;


@Repository
public interface CityRepository extends JpaRepository<City, Integer>{

	Optional<City> findByName(String name);
	
	@Query("SELECT ce FROM City ce WHERE ce.name = ?1 and (?2 IS NULL OR ce.country.name = ?2)")
	Optional<City> findByCityAndCountry(String name, String countryName);
}
