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
 * Clase que representa un DTO (Data Transfer Object) para la entidad Location.
 * Contiene información sobre una locación, incluyendo su ID, nombre, metadatos,
 * compañía asociada, ciudad asociada, estado de actividad y fechas de creación y actualización.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LocationDTO implements ITemplateDTO{
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer locationId;
	
	private String locationName;
	
	private JsonNode locationMeta;
    
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer companyId;
    
	private Integer cityId;
	
}
