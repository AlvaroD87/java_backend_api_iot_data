package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.futuro.api_iot_data.models.Country;


/**
 * Repositorio para la entidad Country que maneja operaciones de acceso a datos.
 * 
 * @see com.futuro.api_iot_data.models.Country
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{

	/**
     * Busca un país por su nombre exacto.
     * 
     * @param name Nombre del país a buscar (case-sensitive)
     * @return {@link Optional} que contiene el país si se encuentra, 
     * o {@link Optional#empty()} si no existe un país con ese nombre
     */
	Optional<Country> findByName(String name);
	
}
