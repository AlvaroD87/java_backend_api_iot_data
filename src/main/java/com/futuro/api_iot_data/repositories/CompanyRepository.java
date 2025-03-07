package com.futuro.api_iot_data.repositories;

import com.futuro.api_iot_data.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Optional<Company> findByCompanyName(String companyName);
}