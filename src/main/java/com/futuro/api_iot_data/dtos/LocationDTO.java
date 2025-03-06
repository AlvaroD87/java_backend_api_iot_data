package com.futuro.api_iot_data.dtos;

import java.sql.Timestamp;
import java.util.Map;


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
public class LocationDTO {
	private Long locationId;
    private String locationName;
    private Map<String, Object> locationMeta;
    private CompanyMockDTO companyDTO;
    private CityMockDTO cityDTO;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
}
