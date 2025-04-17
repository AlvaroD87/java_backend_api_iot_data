package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SensorDTO implements ITemplateDTO{
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer sensorId;
	
	private String sensorName;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String sensorApiKey;
	
	private String sensorCategory;
	
	private JsonNode sensorMeta;
	
	private Integer locationId;
	
}
