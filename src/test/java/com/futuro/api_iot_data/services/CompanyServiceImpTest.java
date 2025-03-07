package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CompanyServiceImpTest {

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private CompanyServiceImp companyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_Success() {
        
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyName("Mi Compañía");
        companyDAO.setAdminId(1);

        Admin admin = new Admin();
        admin.setId(1);

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(companyRepository.findByCompanyName("Mi Compañía")).thenReturn(Optional.empty());
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> invocation.getArgument(0));

        
        ResponseServices response = companyService.createCompany(companyDAO);

        
        assertEquals(201, response.getCode());
        assertEquals("Compañía creada exitosamente", response.getMessage());
        assertNotNull(response.getModelDAO());
    }

    @Test
    void createCompany_AdminNotFound() {
        
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyName("Mi Compañía");
        companyDAO.setAdminId(1);

        when(adminRepository.findById(1)).thenReturn(Optional.empty());

        
        ResponseServices response = companyService.createCompany(companyDAO);

        
        assertEquals(404, response.getCode());
        assertEquals("Admin no encontrado con ID: 1", response.getMessage());
    }

    @Test
    void createCompany_DuplicateName() {
        
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyName("Mi Compañía");
        companyDAO.setAdminId(1);

        Admin admin = new Admin();
        admin.setId(1);

        when(adminRepository.findById(1)).thenReturn(Optional.of(admin));
        when(companyRepository.findByCompanyName("Mi Compañía")).thenReturn(Optional.of(new Company()));

        
        ResponseServices response = companyService.createCompany(companyDAO);

        
        assertEquals(409, response.getCode());
        assertEquals("Ya existe una compañía con el nombre: Mi Compañía", response.getMessage());
    }

    @Test
    void getCompanyById_Success() {
        
        Company company = new Company();
        company.setId(1);
        company.setCompanyName("Mi Compañía");

        when(companyRepository.findById(1)).thenReturn(Optional.of(company));

        
        ResponseServices response = companyService.getCompanyById(1);

        
        assertEquals(200, response.getCode());
        assertEquals("Compañía encontrada", response.getMessage());
        assertNotNull(response.getModelDAO());
    }

    @Test
    void getCompanyById_NotFound() {
        
        when(companyRepository.findById(1)).thenReturn(Optional.empty());

        
        ResponseServices response = companyService.getCompanyById(1);

        
        assertEquals(404, response.getCode());
        assertEquals("Compañía no encontrada con ID: 1", response.getMessage());
    }

    @Test
    void createCompany_MissingCompanyName() {
        
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setAdminId(1);  

        
        ResponseServices response = companyService.createCompany(companyDAO);

        
        assertEquals(400, response.getCode());
        assertEquals("El campo 'companyName' es obligatorio", response.getMessage());
    }
}