package com.futuro.api_iot_data.service;

import com.futuro.api_iot_data.model.Company;
import com.futuro.api_iot_data.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public Company createCompany(String companyName, Admin admin) {
        Company company = new Company();
        company.setCompany_name(companyName);
        company.setCompany_api_key(UUID.randomUUID().toString());
        company.setAdmin(admin);
        company.setIs_active(true);
        company.setCreated_date(LocalDateTime.now());
        company.setUpdate_date(LocalDateTime.now());
        return companyRepository.save(company);
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company updateCompanyName(int companyId, String newName) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found"));
        company.setCompany_name(newName);
        company.setUpdate_date(LocalDateTime.now());
        return companyRepository.save(company);
    }

    public void deleteCompany(int companyId) {
        companyRepository.deleteById(companyId);
    }
}