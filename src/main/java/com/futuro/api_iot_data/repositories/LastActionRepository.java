package com.futuro.api_iot_data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futuro.api_iot_data.models.LastAction;

public interface LastActionRepository extends JpaRepository<LastAction, Integer>{

	LastAction findByActionEnum(String actionEnum);
}
