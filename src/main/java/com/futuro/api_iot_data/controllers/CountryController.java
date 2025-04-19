package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.CountryDTO;
import com.futuro.api_iot_data.services.CountryServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de paises
 */

 @Tag(name="Country Controller", description = "Controlador REST para la gestión de paises")
@RestController
@RequestMapping("/api/v1/country")
public class CountryController {

	@Autowired
	CountryServiceImp countryService;
	
	/**
	 * Obtener el listado de paises registrados en el sistema
	 * @return Listado de paises registrados
	 */
	@GetMapping
	@Operation(summary = "Listado de paises", description = "Obtener el listado de paises registrados en el sistema")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode="200",
			description = "Listado de paises encontrados",
			content = @Content(schema = @Schema(implementation = ResponseServices.class)))
		}
	)
	public ResponseEntity<ResponseServices> listAll(){
		return ResponseEntity.status(200).body(countryService.listAll());
	}
}
