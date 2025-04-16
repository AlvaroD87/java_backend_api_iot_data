package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.futuro.api_iot_data.models.Admin;

/**
 * Repositorio para la entidad Admin que proporciona operaciones de acceso a datos.
 * Extiende JpaRepository para obtener operaciones CRUD básicas automáticamente.
 * 
 * @see com.futuro.api_iot_data.models.Admin
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

	/**
     * Busca un administrador activo por su nombre de usuario usando una consulta SQL nativa
     * 
     * @param username Nombre de usuario del administrador a buscar
     * @return Optional que contiene el Admin si se encuentra y está activo, o vacío si no
     */
	@Query("select a from Admin a where a.username = ?1 and a.isActive = true")
	Optional<Admin> findByUsername(String username);

}
