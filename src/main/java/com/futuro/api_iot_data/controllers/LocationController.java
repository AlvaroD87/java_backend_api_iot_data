package com.futuro.api_iot_data.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.models.DTOs.LocationDTO;
import com.futuro.api_iot_data.services.ILocationService;
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
 * Controlador REST para gestionar las operaciones relacionadas con locaciones.
 * Proporciona endpoints para crear, actualizar, eliminar y consultar locaciones.
 */
@RestController
@RequestMapping("/api/v1/location")
@Tag(name="Location Controller", description="API para la gestión de locaciones")
public class LocationController {
	@Autowired
	private ILocationService locationService;
	
	/**
     * Obtiene el listado de locaciones, en caso de proveer un identificador, 
     * se filtrará solo la locación que corresponda a este identificador
     * @param id Id de la locación a buscar
     * @return ResponseEntity con el DTO de la locación encontrada
     */
	@GetMapping
    @Operation(summary = "Listado de locaciones", 
        description = "Obtiene el listado de locaciones, en caso de proveer un identificador, se filtrará solo la locación que corresponda a este identificador")
    @ApiResponses(
       value = {
        @ApiResponse(responseCode = "200", description = "Listado de locaciones", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "404", description = "No se ha encontrado ninguna locación para el ID espécificado", content = @Content(schema =@Schema(implementation = ResponseServices.class)))
       }
    )
	public ResponseEntity<ResponseServices> findById(
            @Parameter(description = "Api key de la compañía asociada", required = true)
			@RequestHeader(name = "api-key", required = true) 
				String companyApiKey,
			@Parameter(description = "Id de la locación a buscar", required = false) 
			@RequestParam(name = "id", required = false) 
				Integer id
		)
	{
		ResponseServices response = id == null ? locationService.findAll(companyApiKey) : locationService.findById(companyApiKey, id);
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
    public ResponseEntity<ResponseServices> create(
    		@RequestHeader(name = "api-key", required = true) String companyApiKey,
    		@Parameter(description = "Datos de la locación a crear", required = true) @RequestBody LocationDTO locationDTO
    	)
	{
		if(List.of(locationDTO.getLocationName(),
				   locationDTO.getCityId())
			.stream()
			.anyMatch(s -> s == null)
		  )
		{
			return ResponseEntity
					.status(HttpStatus.BAD_REQUEST)
					.body(ResponseServices.builder()
							.code(400)
							.message("locationName y/o cityId son requeridos")
							.modelDTO(locationDTO)
							.build()
						 );
		}
		
        ResponseServices response = locationService.create(locationDTO, companyApiKey);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * Permite actualizar una locación existente
     * @param id Identificador de la locación a actualizar
     * @param locationDTO DTO con la información actualizada de la locación.
     * @return ResponseEntity con los datos actualizados de la locación.
     */
    @PutMapping
    @Operation(summary = "Actualizar una locación existente", description = "Actualiza la información de una locación existente basada en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locación actualizada exitosamente", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "400", description = "Datos de la locación inválidos", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
    })
    public ResponseEntity<ResponseServices> update(
    			@RequestHeader(name = "api-key", required = true) 
    				String companyApiKey,
                @Parameter(description = "ID de la locación a actualizar", required = true) 
    			@RequestParam(name = "id", required = true) 
    				Integer id,
                @Parameter(description = "Datos actualizados de la locación", required = true) 
    				@RequestBody LocationDTO locationDTO
            ){
        ResponseServices response = locationService.update(companyApiKey,id,locationDTO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    /**
     * Permite eliminar una locación por ID
     * @param id Identificador de la locación a eliminar.
     * @return ResponseEntity con los datos de la locación eliminada
     */
    @DeleteMapping
    @Operation(summary = "Eliminar una locación por ID", description = "Elimina una locación específica basada en su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locación eliminada exitosamente", content = @Content(schema = @Schema(implementation = ResponseServices.class))),
        @ApiResponse(responseCode = "400", description = "Error al eliminar la locación", content = @Content(schema =@Schema(implementation = ResponseServices.class) ))
    })
    public ResponseEntity<ResponseServices> deleteById(
    		@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@RequestParam(name = "id", required = true) Integer id
		)
    {
        ResponseServices response = locationService.deleteById(companyApiKey, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
