package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminRepository adminRepository;

    public ResponseServices createCompany(CompanyDAO companyDAO) {
        Optional<Admin> adminOptional = adminRepository.findById(companyDAO.getAdminId());
        if (adminOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Admin not found")
                    .code(404)
                    .build();
        }

        
        String companyApiKey = UUID.randomUUID().toString();

        Company company = new Company();
        company.setCompanyName(companyDAO.getCompanyName());  
        company.setCompanyApiKey(companyApiKey);  
        company.setAdmin(adminOptional.get());  
        company.setIsActive(true);  
        company.setCreatedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));  
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));  

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Company created")
                .code(200)
                .modelDAO(company)
                .build();
    }

    public ResponseServices updateCompany(Integer id, String newName) {
        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Company not found")
                    .code(404)
                    .build();
        }

        Company company = companyOptional.get();
        company.setCompanyName(newName);  
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));  

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Company updated")
                .code(200)
                .build();
    }
}