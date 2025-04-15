package com.futuro.api_iot_data.models.DTOs;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa los datos de un sensor.
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SensorDTO implements ITemplateDTO{
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer sensorId;
	
	@NotBlank(message = "El nombre del sensor es requerido")
	private String sensorName;

	@NotBlank(message = "La categoría del sensor es requerida")
	private String sensorCategory;
	
	//@NotBlank(message = "El ApiKey del sensor es requerida")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String sensorApiKey;
	
	private Map<String,Object> sensorMeta = new HashMap<>();
	
	//@NotNull(message = "El ID de la ubicación es requerida")
	private Integer locationId;
	
	//@NotNull(message = "El estado del sensor es requerido")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Boolean isActive;
	
	//@NotNull(message = "La fecha de creación es requerida")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Timestamp createdDate;
	
	//@NotNull(message = "La fecha de actualización es requerida")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Timestamp updateDate;
	
}
