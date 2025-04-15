package com.futuro.api_iot_data;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.securities.services.PasswordEncoderImp;
import com.futuro.api_iot_data.services.AdminServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

	@Mock
	private AdminRepository adminRepo;
	
	@Mock
	private PasswordEncoderImp passwordService;
	
	@Mock
	private LastActionCacheData lastActionCacheData;
	
	@InjectMocks
	private AdminServiceImp adminService;
	
	private AdminDTO adminDTO;
	private Admin adminEntity;
	private LastAction createdAction;
	
	@BeforeEach
	void setUp() {
		adminDTO = new AdminDTO();
		adminDTO.setUsername("adminUser");
		adminDTO.setPassword("1234");
		
		createdAction = LastAction.builder()
				.id(1)
				.actionEnum("CREATED")
				.actionDesc("Admin creado")
				.build();
		
		adminEntity = Admin.builder()
				.username("adminUser")
				.password("hashed_password")
				.is_active(true)
	            .created_in(new Date(System.currentTimeMillis()))
	            .updated_in(new Date(System.currentTimeMillis()))
	            .lastAction(createdAction)
	            .build();
		
	}
	
	@Test
	void testCreateAdmin_Success() {
		when(adminRepo.findByUsername("adminUser")).thenReturn(Optional.empty());
		when(passwordService.encode("1234")).thenReturn("hashed_password");
		when(lastActionCacheData.getLastAction("CREATED")).thenReturn(createdAction);
		when(adminRepo.save(any(Admin.class))).thenReturn(adminEntity);
		
		ResponseServices response = adminService.create(adminDTO);
		
		assertEquals(200, response.getCode());
		assertEquals("Admin created", response.getMessage());
		assertEquals("adminUser", ((AdminDTO) response.getModelDTO()).getUsername());
		
	}
	
	@Test
	void testCreatedAdmin_UsernameAlreadyExists() {
		when(adminRepo.findByUsername("adminUser")).thenReturn(Optional.of(adminEntity));
		
		ResponseServices response = adminService.create(adminDTO);
		
		assertEquals(300, response.getCode());
		assertEquals("Usuario Duplicado", response.getMessage());
		verify(adminRepo, never()).save(any(Admin.class));
	}
	
	
}
