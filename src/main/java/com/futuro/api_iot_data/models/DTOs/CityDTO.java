package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO que representa los datos de una ciudad para la transferencia entre capas.
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CityDTO implements ITemplateDTO {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer id;
	
	private String name;
	
}
