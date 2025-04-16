package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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

    /**
     * Prueba para crear una compañía exitosamente.
     */
    @Test
    void testCreateCompany_Success() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Example Company");

        when(companyRepository.existsByCompanyName(any(String.class))).thenReturn(false);
        when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(Admin.builder().id(1).build()));
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            company.setId(1);
            company.setCompanyApiKey(UUID.randomUUID().toString());
            return company;
        });

        // Act
        ResponseServices response = companyService.createCompany(companyDTO, "admin");

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Compañía creada exitosamente", response.getMessage());
        assertNotNull(response.getModelDTO());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    /**
     * Prueba para crear una compañía con un nombre que ya existe.
     */
    @Test
    void testCreateCompany_NameAlreadyExists() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Example Company");

        when(companyRepository.existsByCompanyName(any(String.class))).thenReturn(true);

        // Act
        ResponseServices response = companyService.createCompany(companyDTO, "admin");

        // Assert
        assertEquals(400, response.getCode());
        assertEquals("Ya existe una compañía con el mismo nombre", response.getMessage());
        verify(companyRepository, never()).save(any(Company.class));
    }

    /**
     * Prueba para obtener una compañía por ID y API Key exitosamente.
     */
    @Test
    void testGetCompanyById_Success() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        Company company = new Company();
        company.setId(id);
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        // Act
        ResponseServices response = companyService.getCompanyById(id, companyApiKey);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Compañía encontrada", response.getMessage());
        assertNotNull(response.getModelDTO());
        verify(companyRepository, times(1)).findById(id);
    }

    /**
     * Prueba para obtener una compañía por ID y API Key incorrectos.
     */
    @Test
    void testGetCompanyById_NotFoundOrApiKeyMismatch() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "incorrect-api-key";

        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseServices response = companyService.getCompanyById(id, companyApiKey);

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("Compañía no encontrada o API Key incorrecta", response.getMessage());
        verify(companyRepository, times(1)).findById(id);
    }

    /**
     * Prueba para obtener todas las compañías exitosamente.
     */
    @Test
    void testGetAllCompanies_Success() {
        // Arrange
        Company company = new Company();
        company.setId(1);
        company.setCompanyName("Example Company");
        company.setCompanyApiKey("550e8400-e29b-41d4-a716-446655440000");

        when(companyRepository.findAll()).thenReturn(List.of(company));

        // Act
        ResponseServices response = companyService.getAllCompanies("admin");

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Compañías encontradas", response.getMessage());
        assertNotNull(response.getListModelDTO());
        assertEquals(1, ((List<?>) response.getListModelDTO()).size());
        verify(companyRepository, times(1)).findAll();
    }

    /**
     * Prueba para obtener todas las compañías cuando no hay ninguna.
     */
    @Test
    void testGetAllCompanies_NoCompaniesFound() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(List.of());

        // Act
        ResponseServices response = companyService.getAllCompanies("admin");

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("No se encontraron compañías", response.getMessage());
        verify(companyRepository, times(1)).findAll();
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

        Company company = new Company();
        company.setId(id);
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        when(companyRepository.existsByCompanyName(any(String.class))).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Act
        ResponseServices response = companyService.updateCompany(id, companyDTO, companyApiKey);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Compañía actualizada exitosamente", response.getMessage());
        verify(companyRepository, times(1)).findById(id);
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    /**
     * Prueba para actualizar una compañía con un nombre que ya existe.
     */
    @Test
    void testUpdateCompany_NameAlreadyExists() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Existing Company Name");

        Company company = new Company();
        company.setId(id);
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));
        when(companyRepository.existsByCompanyName(any(String.class))).thenReturn(true);

        // Act
        ResponseServices response = companyService.updateCompany(id, companyDTO, companyApiKey);

        // Assert
        assertEquals(400, response.getCode());
        assertEquals("Ya existe una compañía con el mismo nombre", response.getMessage());
        verify(companyRepository, never()).save(any(Company.class));
    }

    /**
     * Prueba para eliminar una compañía exitosamente.
     */
    @Test
    void testDeleteCompany_Success() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "550e8400-e29b-41d4-a716-446655440000";

        Company company = new Company();
        company.setId(id);
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findById(id)).thenReturn(Optional.of(company));

        // Act
        ResponseServices response = companyService.deleteCompany(id, companyApiKey);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Compañía eliminada exitosamente", response.getMessage());
        verify(companyRepository, times(1)).delete(any(Company.class));
    }

    /**
     * Prueba para eliminar una compañía que no existe o con API Key incorrecta.
     */
    @Test
    void testDeleteCompany_NotFoundOrApiKeyMismatch() {
        // Arrange
        Integer id = 1;
        String companyApiKey = "incorrect-api-key";

        when(companyRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseServices response = companyService.deleteCompany(id, companyApiKey);

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("Compañía no encontrada o API Key incorrecta", response.getMessage());
        verify(companyRepository, never()).delete(any(Company.class));
    }
}
