package com.futuro.api_iot_data;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.CompanyServiceImp;
import com.futuro.api_iot_data.services.util.EntityChangeStatusEvent;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {
	
	@Mock
	private CompanyRepository companyRepository;
	
	@Mock
	private AdminRepository adminRepository;
	
	@Mock
	private ApiKeysCacheData apiKeysCacheData;
	
	@Mock
	private LastActionCacheData lastActionCacheData;
	
	@Mock
	private ApplicationEventPublisher eventPublisher;
	
	@InjectMocks
	private CompanyServiceImp companyService;
	
	private CompanyDTO companyDTO;
	private Company company;
	private LastAction lastAction;
	
	@BeforeEach
	void setUp() {
		
		MockitoAnnotations.openMocks(this);
		
		companyService = new CompanyServiceImp(companyRepository);
		
		ReflectionTestUtils.setField(companyService, "companyRepository", companyRepository);
		ReflectionTestUtils.setField(companyService, "adminRepository", adminRepository);
		ReflectionTestUtils.setField(companyService, "apiKeysCacheData", apiKeysCacheData);
		ReflectionTestUtils.setField(companyService, "lastActionCacheData", lastActionCacheData);
		ReflectionTestUtils.setField(companyService, "eventPublisher", eventPublisher);
		
		lastAction = LastAction.builder()
				.id(1)
				.actionEnum("CREATED")
				.actionDesc("Creación")
				.build();
		
		company = new Company();
		company.setId(1);
		company.setCompanyName("Test Company");
		company.setCompanyApiKey("api-key-123");
		company.setIsActive(true);
	}
	
	@Test
	void testCreateCompany_Success() {
		
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName("Test Company");
		
		when(companyRepository.existsByCompanyName("Test Company")).thenReturn(false);
		when(adminRepository.findByUsername("admin")).thenReturn(Optional.of(Admin.builder().id(1).build()));
		when(lastActionCacheData.getLastAction("CREATED")).thenReturn(lastAction);
		when(companyRepository.save(any(Company.class))).thenReturn(company);
		
		ResponseServices response = companyService.createCompany(companyDTO, "admin");
		
		assertEquals(200, response.getCode());
		assertEquals("Compañía creada exitosamente", response.getMessage());
		verify(companyRepository, times(1)).save(any());
	}
	
	@Test
	void testCreateCompany_NameAlreadyExists() {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName("Test Company");
		
		when(companyRepository.existsByCompanyName("Test Company")).thenReturn(true);
		
		ResponseServices response = companyService.createCompany(companyDTO, "admin");
		
		assertEquals(400, response.getCode());
		assertEquals("Ya existe una compañía con el mismo nombre", response.getMessage());
		verify(companyRepository, never()).save(any());
	}
	
	@Test
	void testGetCompanyById_Success() {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName("Test Company");
		
		when(companyRepository.findActiveByIdAndUsername(1, "admin")).thenReturn(Optional.of(company));
		
		ResponseServices response = companyService.getCompanyById(1, "admin");
		
		assertEquals(200, response.getCode());
		assertEquals("Compañía encontrada", response.getMessage());
	}
	
	@Test
	void testGetCompanyById_NotFound() {
		CompanyDTO companyDTO = new CompanyDTO();
		companyDTO.setCompanyName("Test Company");
		
		when(companyRepository.findActiveByIdAndUsername(1, "admin")).thenReturn(Optional.empty());
		
		ResponseServices response = companyService.getCompanyById(1, "admin");
		
		assertEquals(404, response.getCode());
		assertEquals("Compañía no encontrada o API Key incorrecta", response.getMessage());
	}
	
	
	@Test
	void testGetAllCompanies_Empty() {
		when(companyRepository.findAllActiveByUsername("admin")).thenReturn(List.of());
		
		ResponseServices response = companyService.getAllCompanies("admin");
		
		assertEquals(404, response.getCode());
		assertEquals("No se encontraron compañías", response.getMessage());
	}
	
	@Test
	void testUpdateCompany_Success() {
		CompanyDTO updateDTO = new CompanyDTO();
		updateDTO.setCompanyName("Updated Company");
		
		when(companyRepository.findActiveByIdAndUsername(1, "admin")).thenReturn(Optional.of(company));
		when(companyRepository.existsByCompanyName("Updated Company")).thenReturn(false);
		when(lastActionCacheData.getLastAction("UPDATED")).thenReturn(lastAction);
		
		ResponseServices response = companyService.updateCompany(1, updateDTO, "admin");
		
		assertEquals(200, response.getCode());
		assertEquals("Compañía actualizada exitosamente", response.getMessage());
		verify(companyRepository).save(any());
	}
	
	@Test
	void testDeleteCompany_Success() {
		when(companyRepository.findActiveByIdAndUsername(1, "admin")).thenReturn(Optional.of(company));
		when(lastActionCacheData.getLastAction("DELETED")).thenReturn(lastAction);
		when(lastActionCacheData.getLastAction("DELETED_BY_CASCADE")).thenReturn(lastAction);
		
		ResponseServices response = companyService.deleteCompany(1, "admin");
		
		assertEquals(200, response.getCode());
		assertEquals("Compañía eliminada exitosamente", response.getMessage());
		verify(companyRepository).save(any());
		verify(apiKeysCacheData).deleteCompanyApiKey("api-key-123");
		verify(eventPublisher).publishEvent(any(EntityChangeStatusEvent.class));
	}

}
