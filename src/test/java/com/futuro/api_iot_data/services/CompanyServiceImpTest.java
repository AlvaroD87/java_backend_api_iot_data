package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link CompanyServiceImp}.
 * Verifica que la lógica de negocio relacionada con la entidad {@link Company} funcione correctamente.
 * Se utilizan mocks para simular las dependencias, como {@link CompanyRepository}.
 */
class CompanyServiceImpTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImp companyService;

    /**
     * Configuración inicial para cada prueba.
     * Inicializa los mocks y la instancia de {@link CompanyServiceImp}.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Prueba el caso de éxito para crear una compañía.
     * Verifica que se cree una compañía correctamente cuando se proporciona un nombre único.
     */
    @Test
    void createCompany_Success() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Example Company");

        when(companyRepository.existsByCompanyName("Example Company")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenAnswer(invocation -> {
            Company company = invocation.getArgument(0);
            company.setId(1);
            company.setCompanyApiKey(UUID.randomUUID().toString());
            return company;
        });

        // Act
        ResponseServices response = companyService.createCompany(companyDTO);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Company created successfully", response.getMessage());
        assertNotNull(response.getModelDTO());
    }

    /**
     * Prueba el caso de error al crear una compañía con un nombre duplicado.
     * Verifica que se devuelva un mensaje de error cuando el nombre de la compañía ya existe.
     */
    @Test
    void createCompany_Error_DuplicateName() {
        // Arrange
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Example Company");

        when(companyRepository.existsByCompanyName("Example Company")).thenReturn(true);

        // Act
        ResponseServices response = companyService.createCompany(companyDTO);

        // Assert
        assertEquals(400, response.getCode());
        assertEquals("A company with the same name already exists", response.getMessage());
    }

    /**
     * Prueba el caso de éxito para obtener una compañía por su API Key.
     * Verifica que se devuelva la compañía correcta cuando se proporciona un API Key válido.
     */
    @Test
    void getCompanyByApiKey_Success() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();
        Company company = new Company();
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.of(company));

        // Act
        ResponseServices response = companyService.getCompanyByApiKey(companyApiKey);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Company found", response.getMessage());
        assertNotNull(response.getModelDTO());
    }

    /**
     * Prueba el caso de error al obtener una compañía con un API Key incorrecto.
     * Verifica que se devuelva un mensaje de error cuando no se encuentra la compañía.
     */
    @Test
    void getCompanyByApiKey_Error_NotFound() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.empty());

        // Act
        ResponseServices response = companyService.getCompanyByApiKey(companyApiKey);

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("Company not found with the provided API Key", response.getMessage());
    }

    /**
     * Prueba el caso de éxito para obtener todas las compañías.
     * Verifica que se devuelva la lista de compañías cuando hay al menos una registrada.
     */
    @Test
    void getAllCompanies_Success() {
        // Arrange
        Company company = new Company();
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(UUID.randomUUID().toString());

        when(companyRepository.findAll()).thenReturn(List.of(company));

        // Act
        ResponseServices response = companyService.getAllCompanies();

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Companies found", response.getMessage());
        assertNotNull(response.getListModelDTO());
    }

    /**
     * Prueba el caso de error al obtener todas las compañías cuando no hay ninguna registrada.
     * Verifica que se devuelva un mensaje de error cuando no hay compañías.
     */
    @Test
    void getAllCompanies_Error_NoCompanies() {
        // Arrange
        when(companyRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ResponseServices response = companyService.getAllCompanies();

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("No companies found", response.getMessage());
    }

    /**
     * Prueba el caso de éxito para actualizar una compañía.
     * Verifica que se actualice el nombre de la compañía correctamente.
     */
    @Test
    void updateCompany_Success() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Updated Company Name");

        Company company = new Company();
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.of(company));
        when(companyRepository.existsByCompanyName("Updated Company Name")).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        // Act
        ResponseServices response = companyService.updateCompany(companyApiKey, companyDTO);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Company updated successfully", response.getMessage());
    }

    /**
     * Prueba el caso de error al actualizar una compañía con un nombre duplicado.
     * Verifica que se devuelva un mensaje de error cuando el nombre de la compañía ya existe.
     */
    @Test
    void updateCompany_Error_DuplicateName() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("Updated Company Name");

        Company company = new Company();
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.of(company));
        when(companyRepository.existsByCompanyName("Updated Company Name")).thenReturn(true);

        // Act
        ResponseServices response = companyService.updateCompany(companyApiKey, companyDTO);

        // Assert
        assertEquals(400, response.getCode());
        assertEquals("A company with the same name already exists", response.getMessage());
    }

    /**
     * Prueba el caso de éxito para eliminar una compañía.
     * Verifica que se elimine la compañía correctamente.
     */
    @Test
    void deleteCompany_Success() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();
        Company company = new Company();
        company.setCompanyName("Example Company");
        company.setCompanyApiKey(companyApiKey);

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.of(company));

        // Act
        ResponseServices response = companyService.deleteCompany(companyApiKey);

        // Assert
        assertEquals(200, response.getCode());
        assertEquals("Company deleted successfully", response.getMessage());
        verify(companyRepository, times(1)).delete(company);
    }

    /**
     * Prueba el caso de error al eliminar una compañía con un API Key incorrecto.
     * Verifica que se devuelva un mensaje de error cuando no se encuentra la compañía.
     */
    @Test
    void deleteCompany_Error_NotFound() {
        // Arrange
        String companyApiKey = UUID.randomUUID().toString();

        when(companyRepository.findByCompanyApiKey(companyApiKey)).thenReturn(Optional.empty());

        // Act
        ResponseServices response = companyService.deleteCompany(companyApiKey);

        // Assert
        assertEquals(404, response.getCode());
        assertEquals("Company not found with the provided API Key", response.getMessage());
    }
}