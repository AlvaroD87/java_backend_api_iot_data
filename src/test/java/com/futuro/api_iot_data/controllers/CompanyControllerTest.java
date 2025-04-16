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
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para CompanyController.
 * 
 * <p>Verifica el comportamiento del controlador para las operaciones CRUD
 * de compañías, incluyendo casos de éxito y manejo de errores.</p>
 */
class CompanyControllerTest {

    @Mock
    private ICompanyService companyService;

    @Mock
    private UserDetails userDetailMock;
    
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

        when(userDetailMock.getUsername()).thenReturn("admin");
        when(companyService.createCompany(any(CompanyDTO.class),any(String.class))).thenReturn(responseServices);

        // Act
        ResponseEntity<ResponseServices> response = companyController.createCompany(companyDTO, userDetailMock);

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

        when(userDetailMock.getUsername()).thenReturn("admin");
        when(companyService.getCompanyById(id, userDetailMock.getUsername())).thenReturn(responseServices);
        
        // Act
        ResponseEntity<ResponseServices> response = companyController.getCompanyById(id, userDetailMock);

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
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        when(userDetailMock.getUsername()).thenReturn("admin");
        when(companyService.getAllCompanies(userDetailMock.getUsername())).thenReturn(responseServices);
        
        
        // Act
        ResponseEntity<ResponseServices> response = companyController.getCompanyById(null, userDetailMock);

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

        when(userDetailMock.getUsername()).thenReturn("admin");
        when(companyService.updateCompany(id, companyDTO, userDetailMock.getUsername())).thenReturn(responseServices);
        
        // Act
        ResponseEntity<ResponseServices> response = companyController.updateCompany(id, companyDTO, userDetailMock);

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

        when(userDetailMock.getUsername()).thenReturn("admin");
        when(companyService.deleteCompany(id, userDetailMock.getUsername())).thenReturn(responseServices);
        
        // Act
        ResponseEntity<ResponseServices> response = companyController.deleteCompany(id, userDetailMock);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(200, response.getBody().getCode());
        assertEquals("Company deleted successfully", response.getBody().getMessage());
    }
}
