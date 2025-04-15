package com.futuro.api_iot_data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futuro.api_iot_data.models.LastAction;

/**
 * Repositorio para la entidad {@link LastAction} que registra las últimas acciones del sistema.
 * 
 */
public interface LastActionRepository extends JpaRepository<LastAction, Integer>{

	/**
     * Busca una acción registrada por su nombre.
     *
     * @param actionEnum Nombre de la acción
     * @return La entidad {@link LastAction} correspondiente al valor enumerado,
     *         o {@code null} si no se encuentra ninguna acción con ese valor
     */
	LastAction findByActionEnum(String actionEnum);
}
