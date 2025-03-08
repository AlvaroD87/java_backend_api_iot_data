package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Country;


@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{

	Optional<Country> findByName(String name);
	
}
