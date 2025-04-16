package com.futuro.api_iot_data.models.DTOs;

import java.sql.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
	
	//@NotBlank(message = "El nombre de la locación es obligatorio")
	private String locationName;
	
	//@NotEmpty(message = "Las metadatas de la locación son obligatorias")
    private Map<String, Object> locationMeta;
    
    // private CompanyDTO companyDTO;
	//@NotNull(message = "La compañía es obligatoria")
    private Integer companyId;
    
    // private CityDTO cityDTO;
	//@NotNull(message = "La ciudad es obligatoria")
	private Integer cityId;
    
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isActive;
    
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date createdDate;
    
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date updateDate;
}
