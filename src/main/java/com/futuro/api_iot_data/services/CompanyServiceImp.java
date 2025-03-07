package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CompanyServiceImp implements ICompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public ResponseServices createCompany(CompanyDAO companyDAO) {
        if (companyDAO.getCompanyName() == null || companyDAO.getCompanyName().isEmpty()) {
            return ResponseServices.builder()
                    .message("El campo 'companyName' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        if (companyDAO.getAdminId() == null) {
            return ResponseServices.builder()
                    .message("El campo 'adminId' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        Optional<Company> existingCompany = companyRepository.findByCompanyName(companyDAO.getCompanyName());
        if (existingCompany.isPresent()) {
            return ResponseServices.builder()
                    .message("Ya existe una compañía con el nombre: " + companyDAO.getCompanyName())
                    .code(HttpStatus.CONFLICT.value())
                    .build();
        }

        Optional<Admin> adminOptional = adminRepository.findById(companyDAO.getAdminId());
        if (adminOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Admin no encontrado con ID: " + companyDAO.getAdminId())
                    .code(HttpStatus.NOT_FOUND.value())
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
                .message("Compañía creada exitosamente")
                .code(HttpStatus.CREATED.value())
                .modelDAO(company)
                .build();
    }

    @Override
    public ResponseServices getCompanyById(Integer id) {
        if (id == null) {
            return ResponseServices.builder()
                    .message("El campo 'id' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada con ID: " + id)
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        return ResponseServices.builder()
                .message("Compañía encontrada")
                .code(HttpStatus.OK.value())
                .modelDAO(companyOptional.get())
                .build();
    }

    @Override
    public ResponseServices updateCompany(Integer id, CompanyDAO companyDAO) {
        if (id == null) {
            return ResponseServices.builder()
                    .message("El campo 'id' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        if (companyDAO.getCompanyName() == null || companyDAO.getCompanyName().isEmpty()) {
            return ResponseServices.builder()
                    .message("El campo 'companyName' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada con ID: " + id)
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        Company company = companyOptional.get();
        company.setCompanyName(companyDAO.getCompanyName());
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Compañía actualizada exitosamente")
                .code(HttpStatus.OK.value())
                .build();
    }

    @Override
    public ResponseServices deleteCompany(Integer id) {
        if (id == null) {
            return ResponseServices.builder()
                    .message("El campo 'id' es obligatorio")
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        Optional<Company> companyOptional = companyRepository.findById(id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada con ID: " + id)
                    .code(HttpStatus.NOT_FOUND.value())
                    .build();
        }

        companyRepository.delete(companyOptional.get());

        return ResponseServices.builder()
                .message("Compañía eliminada exitosamente")
                .code(HttpStatus.OK.value())
                .build();
    }
    
    @Override
    public ResponseServices getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        return ResponseServices.builder()
                .message("Compañías encontradas")
                .code(HttpStatus.OK.value())
                .modelDAO(companies)
                .build();
    }
}