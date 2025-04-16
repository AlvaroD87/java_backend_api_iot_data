package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.City;


/**
 * Repositorio para la entidad City que proporciona operaciones de acceso a datos.
 * 
 * @see com.futuro.api_iot_data.models.City
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer>{

	/**
	 * Busca una ciudad por su nombre exacto.
	 * 
	 * @param name Nombre de la ciudad a buscar
	 * @return {@link Optional} que contiene la ciudad si se encuentra, o vacío si no
 	*/
	Optional<City> findByName(String name);
	
	/**
	 * Busca una ciudad por nombre y opcionalmente por país.
	 *
	 * @param name Nombre de la ciudad (requerido)
	 * @param countryName Nombre del país (opcional, puede ser null)
	 * @return {@link Optional} que contiene la ciudad si se encuentra con los criterios,
	 *         o vacío si no
	 * 
	*/
	@Query("SELECT ce FROM City ce WHERE ce.name = ?1 and (?2 IS NULL OR ce.country.name = ?2)")
	Optional<City> findByCityAndCountry(String name, String countryName);
}
