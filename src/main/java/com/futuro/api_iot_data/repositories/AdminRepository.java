package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.futuro.api_iot_data.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

	@Query("select a from Admin a where a.username = ?1 and a.isActive = true")
	Optional<Admin> findByUsername(String username);

}
