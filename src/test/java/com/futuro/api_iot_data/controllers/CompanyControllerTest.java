package com.futuro.api_iot_data.controllers;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
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
import static org.mockito.ArgumentMatchers.any;
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

    /**
     * Prueba para crear una compañía exitosamente.
     */
    @Test
    void testCreateCompany_Success() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Example Company");

        ResponseServices responseServices = ResponseServices.builder()
                .code(200)
                .message("Company created successfully")
                .build();

        when(companyService.createCompany(any(CompanyDTO.class))).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.createCompany(companyDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Company created successfully", response.getBody().getMessage());
    }

    /**
     * Prueba para obtener una compañía por ID y API Key exitosamente.
     */
    @Test
    void testGetCompanyById_Success() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        ResponseServices responseServices = ResponseServices.builder()
                .code(200)
                .message("Company found")
                .build();

        when(companyService.getCompanyById(id, companyApiKey)).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.getCompanyById(id, companyApiKey);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Company found", response.getBody().getMessage());
    }

    /**
     * Prueba para obtener todas las compañías exitosamente.
     */
    @Test
    void testGetAllCompanies_Success() {
        // Arrange
        ResponseServices responseServices = ResponseServices.builder()
                .code(200)
                .message("Companies found")
                .build();

        when(companyService.getAllCompanies()).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.getAllCompanies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Companies found", response.getBody().getMessage());
    }

    /**
     * Prueba para actualizar una compañía exitosamente.
     */
    @Test
    void testUpdateCompany_Success() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Updated Company Name");

        ResponseServices responseServices = ResponseServices.builder()
                .code(200)
                .message("Company updated successfully")
                .build();

        when(companyService.updateCompany(id, companyDTO, companyApiKey)).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.updateCompany(id, companyDTO, companyApiKey);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Company updated successfully", response.getBody().getMessage());
    }

    /**
     * Prueba para eliminar una compañía exitosamente.
     */
    @Test
    void testDeleteCompany_Success() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        ResponseServices responseServices = ResponseServices.builder()
                .code(200)
                .message("Company deleted successfully")
                .build();

        when(companyService.deleteCompany(id, companyApiKey)).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.deleteCompany(id, companyApiKey);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Company deleted successfully", response.getBody().getMessage());
    }
}
