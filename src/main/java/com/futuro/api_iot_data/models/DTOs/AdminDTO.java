package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa los datos de un administrador para la transferencia entre capas.
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminDTO implements ITemplateDTO{
	
	private String username;
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

}
