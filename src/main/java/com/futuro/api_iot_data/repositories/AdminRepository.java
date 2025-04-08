package com.futuro.api_iot_data.repositories;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;

import com.futuro.api_iot_data.models.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

	@NativeQuery("select * from admins where username = ?1 and is_active = true")
	Optional<Admin> findByUsername(String username);

}
