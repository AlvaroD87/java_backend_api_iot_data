package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.services.AdminServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST que proporciona endpoints para la gestión de administradores del sistema
 */
@Tag(name="Admin Controller", description="Controlador REST que proporciona endpoints para la gestión de administradores del sistema")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

	@Autowired
	AdminServiceImp adminService;
	
	/**
	 * Permite crear un nuevo administrador en el sistema
	 * @param newAdmin Datos del nuevo administrador
	 * @return ResponseServices con el resultado de la operación
	 */
	@PostMapping//("/create")
	@Operation(summary = "Creación de nuevos administradores",
		description= "Permite crear un nuevo administrador en el sistema")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "Administrador creado con éxito", 
				content = @Content(schema = @Schema(implementation = ResponseServices.class))),
			@ApiResponse(responseCode = "400", description = "Los datos ingresados no son válidos", 
				content = @Content(schema = @Schema(implementation = ResponseServices.class)))
		}
	)
	public ResponseEntity<ResponseServices> createAdmin(
		@Parameter(description = "Datos del nuevo administrador")
		@RequestBody @Valid AdminDTO newAdmin){
		
		ResponseServices response = adminService.create(newAdmin);
		
		return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
	}
	
	
}
