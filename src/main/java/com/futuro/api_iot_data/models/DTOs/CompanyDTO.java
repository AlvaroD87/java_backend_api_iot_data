package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para la entidad Company.
 * Contiene la información necesaria para crear o actualizar una compañía.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompanyDTO implements ITemplateDTO {
    
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer companyId;
	
	/**
     * Nombre de la compañía.
     */
	@NotBlank(message="CompanyName es requerido")
    private String companyName;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String companyApiKey;
}
