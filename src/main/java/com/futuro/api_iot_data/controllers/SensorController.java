package com.futuro.api_iot_data.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.SensorDTO;
import com.futuro.api_iot_data.services.SensorService;
import com.futuro.api_iot_data.services.util.ResponseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Controlador REST para la gestión de sensores.
 * Ofrece endpoints para el listado, creación, edición y eliminación de sensores.
 */
@Tag(name="Sensor controller", description = "Controlador REST para la gestión de sensores")
@RestController
@RequestMapping("/api/v1/sensor")
public class SensorController {
	
	@Autowired
	private SensorService sensorService;
	
	/*
	 *	@RequestHeader(name = "api-key", required = true) String companyApiKey,
	 *	@RequestParam(name = "from", required = false) Integer fromEpoch,
	 */
	
	/*@GetMapping("/all")
	public ResponseEntity<ResponseServices> getAllSensors() {
		ResponseServices response = sensorService.getAllSensors();
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}*/
	
	/**
	 * Permite obtener un sensor a partir de su identificador
	 * @param companyApiKey API key de la compañía asociada a este sensor
	 * @param id Identificador único del sensor a buscar
	 * @return Datos del sensor encontrado
	 */
	@GetMapping//("/{id}")
	@Operation(summary = "Listado de sensores", description = "Permite obtener un sensor a partir de su identificador")
	@ApiResponses(
		value = {
			@ApiResponse(responseCode = "200", description = "Datos del sensor encontrado",
		content = @Content(schema = @Schema(implementation = ResponseServices.class))),
		@ApiResponse(responseCode = "404", description = "No se ha encontrado ningún sensor para los filtros proporcionados",
		content = @Content(schema = @Schema(implementation = ResponseServices.class)))
		}
	)
	public ResponseEntity<ResponseServices> getSensorById(
			@Parameter(description = "API key de la compañía asociada a este sensor")
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@Parameter(description = "Identificador único del sensor a buscar" )
			@RequestParam(name = "id", required = false) Integer id) 
	{
		ResponseServices response = id == null ? sensorService.getAllSensors(companyApiKey) : sensorService.getSensorById(companyApiKey,id);
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	/**
	 * Permite registrar un nuevo sensor en el sistema
	 * @param companyApiKey API KEY de la compañía asociada al nuevo sensor
	 * @param sensorDTO Datos del nuevo sensor
	 * @return ResponseServices con los datos del sensor creado
	 */
	@PostMapping//("/create")
	@Operation(summary = "Registro de un nuevo sensor", description = "Permite registrar un nuevo sensor en el sistema")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "201",
				description = "El sensor se ha creado con éxito",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			),
			@ApiResponse(
				responseCode = "409",
				description = "Algunos datos del sensor ya han sido registrados",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "Los datos del sensor no son válidos",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			)
		}
	)
	public ResponseEntity<ResponseServices> createSensor(
		@Parameter(description = "API KEY de la compañía asociada al nuevo sensor")
		@RequestHeader(name = "api-key", required = true) String companyApiKey, 
		@Parameter(description = "Datos del nuevo sensor")
		@Valid @RequestBody SensorDTO sensorDTO) 
	{
		ResponseServices response = sensorService.createSensor(companyApiKey, sensorDTO);
		if(response.getCode() == 201) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
	}
	
	/**
	 * Permite actualizar un sensor ya registrado
	 * @param companyApiKey API KEY de la compañía asociada a este sensor
	 * @param id Identificador único del sensor a actualizar
	 * @param sensorDTO Datos actualizados del sensor
	 * @return ResponseEntity con los datos del sensor
	 */
	@PutMapping//("/{id}")
	@Operation(summary = "Actualizar un sensor", description = "Permite actualizar un sensor existente en el sistema")
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200",
				description = "El sensor se ha actualizado con éxito",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "Los datos del sensor no son válidos",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "No se ha encontrado ningún sensor con el ID proporcionado",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			)
		}
	)
	public ResponseEntity<ResponseServices> updateSensor(
			@Parameter(description = "API KEY de la compañía asociada al sensor")
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@Parameter(description = "Id del sensor a actualizar")
			@RequestParam(name = "id", required = false) Integer id, 
			@Valid @RequestBody SensorDTO sensorDTO
		) 
	{
		ResponseServices response = sensorService.updateSensor(id, sensorDTO, companyApiKey);
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	/**
	 * Permite eliminar un sensor del sistema
	 * @param id Identificador del sensor a eliminar
	 * @return Resultado de la operación
	 */
	@DeleteMapping//("/{id}")
	@Operation(
		summary = "Eliminar un sensor",
		description = "Permite eliminar un sensor en el sistema"
	)
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200",
				description = "El sensor se ha eliminado con éxito",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			),
			@ApiResponse(
				responseCode = "404",
				description = "No se ha encontrado ningún sensor con el id específicado",
				content = @Content(schema = @Schema(implementation = ResponseEntity.class))
			)
		}
	)
	public ResponseEntity<ResponseServices> deleteSensor(
		@Parameter(description = "Identificador del sensor a eliminar")
		@RequestParam(name = "sensor_id", required = true) Integer id) {
		ResponseServices response = sensorService.deleteSensor(id);
		
		if(response.getCode() == 200) {
			return ResponseEntity.ok(response);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	
	/**
	 * Método personalizado para el control de excepciones para los datos de sensores inválidos
	 * @param ex Excepción capturada
	 * @return ResponseEntity con la información de la excepcion capturada
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.BAD_REQUEST.value());
		
		Map<String, String> errors = new HashMap<>();
		for(FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.put(error.getField(), error.getDefaultMessage());
		}
		
		response.put("errors", errors);
		return ResponseEntity.badRequest().body(response);
	}
	
	/**
	 * Método personalizado para el control de excepciones para un sensor no encontrado
	 * @param ex Excepción capturada
	 * @return ResponseEntity con la información de la excepcion capturada
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Map<String, Object>> handleSensorNotFound(IllegalArgumentException ex) {
		Map<String, Object> response = new HashMap<>();
		response.put("status", HttpStatus.NOT_FOUND.value());
		response.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
	 
}
