package com.futuro.api_iot_data.controllers;

import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.services.ICompanyService;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompanyControllerTest {

    @Mock
    private ICompanyService companyService;

    @InjectMocks
    private CompanyController companyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCompany_Success() {
        
        CompanyDAO companyDAO = new CompanyDAO();
        companyDAO.setCompanyName("Mi Compañía");
        companyDAO.setAdminId(1);

        ResponseServices responseServices = ResponseServices.builder()
                .message("Compañía creada exitosamente")
                .code(HttpStatus.CREATED.value())
                .build();

        when(companyService.createCompany(companyDAO)).thenReturn(responseServices);

        
        ResponseEntity<ResponseServices> response = companyController.createCompany(companyDAO);

        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Compañía creada exitosamente", response.getBody().getMessage());
    }

    @Test
    void getCompanyById_Success() {
        
        ResponseServices responseServices = ResponseServices.builder()
                .message("Compañía encontrada")
                .code(HttpStatus.OK.value())
                .build();

        when(companyService.getCompanyById(1)).thenReturn(responseServices);

        
        ResponseEntity<ResponseServices> response = companyController.getCompanyById(1);

        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Compañía encontrada", response.getBody().getMessage());
    }
}