package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa los datos de un país para la transferencia entre capas.
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CountryDTO implements ITemplateDTO {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer countryId;
	
	private String name;
	
}
