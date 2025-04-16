package com.futuro.api_iot_data.models.DTOs;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.models.Sensor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa los datos almacenados de un sensor.
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SensorDataDTO implements ITemplateDTO {
	
	private JsonNode data;
	private Integer sensorId;
	private Sensor sensor;
	private Instant createdEpoch;
	
}
