package com.futuro.api_iot_data.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SensorDTO {
	
	private Integer sensorId;
	
	@NotBlank(message = "El nombre del sensor es requerido")
	private String sensorName;

	@NotBlank(message = "La categoría del sensor es requerida")
	private String sensorCategory;
	
	@NotBlank(message = "El ApiKey del sensor es requerida")
	private String sensorApiKey;
	
	private Map<String,Object> sensorMeta = new HashMap<>();
	
	@NotNull(message = "El ID de la ubicación es requerida")
	private Integer locationId;
	
	@NotNull(message = "El estado del sensor es requerido")
	private Boolean isActive;
	
	@NotNull(message = "La fecha de creación es requerida")
	private Timestamp createdDate;
	
	@NotNull(message = "La fecha de actualización es requerida")
	private Timestamp updateDate;
	
}
