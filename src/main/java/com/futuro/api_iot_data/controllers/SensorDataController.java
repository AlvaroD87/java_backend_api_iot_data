package com.futuro.api_iot_data.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.futuro.api_iot_data.services.SensorDataServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controlador REST para la gestión de sensores
 */
@Tag(name="Sensor Data Controller", description = "Controlador REST para la gestión de datos de sensores")
@RestController
@RequestMapping("/api/v1/sensor_data")
public class SensorDataController {

	@Autowired
	SensorDataServiceImp sensorDataService;
	
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	/**
	 * Permite almacenar datos de sensores
	 * @param jsonBody JSON con los datos a almacenar, debe estar en el formato de datos correcto
	 * @return Resultado de la operación
	 */
	@PostMapping//("/insert-data")
	@Operation(
		summary = "Insertar nuevos datos de sensor",
		description = "Permite insertar nuevos registros para un sensor"
	)
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200",
				description = "Los datos se han guardado con éxito",
				content = @Content(schema = @Schema(implementation = ResponseServices.class))
			),
			@ApiResponse(
				responseCode = "400",
				description = "Los datos ingresados no son válidos",
				content = @Content(schema = @Schema(implementation = ResponseServices.class))
			)
		}
	)
	public ResponseEntity<ResponseServices> insertData(
		@Parameter(description = "JSON con los datos de sensor a ingresar")
		@RequestBody JsonNode jsonBody) {
		
		JsonNode jsonApiKey = jsonBody.get("api_key");
		JsonNode jsonData = jsonBody.get("json_data");
		
		if(jsonData == null || jsonApiKey == null ) {
			return ResponseEntity
					.status(HttpStatus.OK) // TO-DO Modificar por mejor código de status
					.body(ResponseServices.builder()
							.code(300)
							.message("El body de la solicitud inválido")
					.build());
		}
		
		List<JsonNode> dataList = jsonData.isArray() ? StreamSupport.stream(jsonData.spliterator(), false).collect(Collectors.toList()) : List.of(jsonData);
		String sensorApiKey = jsonApiKey.asText();
		
		ResponseServices response = sensorDataService.insertData(sensorApiKey, dataList);
		
		return ResponseEntity
				.status(response.getCode() == 400 ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
				.body(response);
	}
	
	/**
	 * Obtiene los datos registrados para el sensor indicado
	 * @param companyApiKey API KEY de la compañía asociado al sensor
	 * @param fromEpoch Fecha mínima para el filtrado de datos
	 * @param toEpoch Fecha máxima para el filtrado de datos
	 * @param sensorId IDs de sensores deseados separados por coma.
	 * @param sensorCategory IDs de categorías deseadas separados por coma.
	 * @return Datos del sensor encontrado
	 */
	@GetMapping//("/get-data")
	@Operation(
		summary = "Obtener los datos de un sensor",
		description = "Permite obtener los datos guardados para un sensor"
	)
	@ApiResponses(
		value = {
			@ApiResponse(
				responseCode = "200",
				description = "Datos encontrados para el sensor",
				content = @Content(schema = @Schema(implementation = ResponseServices.class))
			),
		}
	)
	public ResponseEntity<ResponseServices> getData(
			@Parameter(description = "API KEY de la compañía asociada al sensor")
			@RequestHeader(name = "api-key", required = true) String companyApiKey,
			@Parameter(description = "Fecha mínima deseada")
			@RequestParam(name = "from", required = false) Integer fromEpoch,
			@Parameter(description = "Fecha máxima deseada")
			@RequestParam(name = "to", required = false) Integer toEpoch,
			@Parameter(description = "IDs de sensores deseados separados por coma.")
			@RequestParam(name = "sensor_id", required = false, defaultValue = "") List<Integer> sensorId,
			@Parameter(description = "IDs de categorías deseadas separados por coma.")
			@RequestParam(name = "sensor_category", required = false, defaultValue = "") List<String> sensorCategory
			)
	{
		ObjectNode parameters = objectMapper.createObjectNode();
		
		parameters.put("companyApiKey", companyApiKey);
		parameters.put("fromEpoch", fromEpoch);
		parameters.put("toEpoch", toEpoch);
		
		ArrayNode arraySensorId = objectMapper.createArrayNode();
		sensorId.stream().filter(i -> i != null).forEach(s -> arraySensorId.add(s));
		parameters.set("sensorId", arraySensorId);
		
		ArrayNode arraySensorCategory = objectMapper.createArrayNode();
		sensorCategory.stream().filter(s -> !s.isBlank()).forEach(c -> arraySensorCategory.add(c));
		parameters.set("sensorCategory", arraySensorCategory);
		
		return ResponseEntity.status(HttpStatus.OK).body(sensorDataService.getData(parameters));
	}
}
