package com.futuro.api_iot_data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.CityDTO;
import com.futuro.api_iot_data.services.CityServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de ciudades del sistema
 */
@Tag(name="City Controller", description="Controlador REST para la gestión de ciudades del sistema")
@RestController
@RequestMapping("/api/v1/city")
public class CityController {

	@Autowired
	CityServiceImp cityService;

	/*@PostMapping("/create")
	public ResponseEntity<ResponseServices> create(@RequestBody CityDTO newCity){
		
		ResponseServices response = cityService.create(newCity);
		
		return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(response);
	}*/

	/**
	 * Obtener todas las ciudades registradas
	 * @return Listado de ciudades registradas
	 */
	@GetMapping//("/get-all")
	@Operation(summary="Listado de ciudades", description = "Obtener todas las ciudades registradas")
	@ApiResponses(value = {
		@ApiResponse(responseCode ="200", description = "Listado de ciudades encontradas",
		content = @Content(schema = @Schema(implementation = ResponseServices.class)) 
		)
	})
	public ResponseEntity<ResponseServices> getAll(){
		return ResponseEntity.ok(cityService.listAll());
	}
}
