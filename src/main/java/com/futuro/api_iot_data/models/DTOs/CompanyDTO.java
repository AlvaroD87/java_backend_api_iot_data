package com.futuro.api_iot_data.models.DTOs;

import lombok.Data;

/**
 * DTO (Data Transfer Object) para la entidad Company.
 * Contiene la información necesaria para crear o actualizar una compañía.
 */
@Data
public class CompanyDTO implements ITemplateDTO {
    /**
     * Nombre de la compañía.
     */
    private String companyName;
}
