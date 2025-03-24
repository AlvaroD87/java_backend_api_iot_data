package com.futuro.api_iot_data.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.dtos.LocationDTO;
import com.futuro.api_iot_data.services.ILocationService;
import com.futuro.api_iot_data.services.util.ResponseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para gestionar las operaciones relacionadas con locaciones.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar locaciones.
 */
@RestController
@RequestMapping("/locations")
@Tag(name="Location Controller", description="API para la gestión de locaciones")
public class LocationController {
	@Autowired
	private ILocationService locationService;
	
    /**
     * Obtiene una lista con todas las locaciones registradas.
     * @return ResponseEntity con la lista de locaciones
     */
	@GetMapping
	@Operation(summary="Obtener el listado de locaciones", description = "Retorna una lista con todas las locaciones registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de locaciones encontradas",content = @Content(schema = @Schema(implementation = ResponseServices.class))),
    })
	public ResponseEntity<ResponseServices> findAll(){
		ResponseServices response = locationService.findAll();
		return ResponseEntity.status(response.getCode()).body(response);
	}
	/**
     * Obtener una locación a partir de su identificador
     * @param id Id de la locación a buscar
     * @return ResponseEntity con el DTO de la locación encontrada
     */
	@GetMapping("/{id}")
    @Operation(summary = "Obtener una locación a partir de su identificador", description = "Devuelve una locación específica basada en su identificador")
    @ApiResponses(
       value = {
        @ApiResponse(responseCode = "200", description = "Locación encontrada", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna locación para el ID espécificado", content = @Content(schema =@Schema(implementation = ResponseServices.class)))
       }
    )
	public ResponseEntity<ResponseServices> findById(@Parameter(description = "Id de la locación a buscar", required = true) @PathVariable Integer id){
		ResponseServices response = locationService.findById(id);
		return ResponseEntity.status(response.getCode()).body(response);
	}
	
    /**
     * Permite crear una nueva locación
     * @param locationDTO DTO con la información de la locación a crear
     * @return ResonseEntity con el DTO de la locación creada
     */
	@PostMapping
    @Operation(summary = "Crear una nueva locación", description = "Crea una nueva locación con la información proporcionada.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Locación creada exitosamente", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la locación inválidos", content = @Content(schema =@Schema(implementation = ResponseServices.class)))
    })
    public ResponseEntity<ResponseServices> create(@Parameter(description = "Datos de la locación a crear", required = true) @RequestBody LocationDTO locationDTO){
        ResponseServices response = locationService.create(locationDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * Permite actualizar una locación existente
     * @param id Identificador de la locación a actualizar
     * @param locationDTO DTO con la información actualizada de la locación.
     * @return ResponseEntity con los datos actualizados de la locación.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una locación existente", description = "Actualiza la información de una locación existente basada en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locación actualizada exitosamente", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la locación inválidos", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
    })
    public ResponseEntity<ResponseServices> update(
                @Parameter(description = "ID de la locación a actualizar", required = true) @PathVariable Integer id,
                @Parameter(description = "Datos actualizados de la locación", required = true) @RequestBody LocationDTO locationDTO
            ){
        ResponseServices response = locationService.update(id,locationDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * Permite eliminar una locación por ID
     * @param id Identificador de la locación a eliminar.
     * @return ResponseEntity con los datos de la locación eliminada
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una locación por ID", description = "Elimina una locación específica basada en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locación eliminada exitosamente", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "400", description = "Error al eliminar la locación", content = @Content(schema =@Schema(implementation = ResponseServices.class) ))
    })
    public ResponseEntity<ResponseServices> deleteById(@PathVariable Integer id){
        ResponseServices response = locationService.deleteById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
