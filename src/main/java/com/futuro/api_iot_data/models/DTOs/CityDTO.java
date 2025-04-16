package com.futuro.api_iot_data.models.DTOs;

import com.futuro.api_iot_data.models.City;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO que representa los datos de una ciudad para la transferencia entre capas.
*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CityDTO implements ITemplateDTO {

	private String name;

	private CountryDTO country;
	
	public CityDTO(City city) {
		this.name = city.getName();
		this.country = city.getCountry().toCountryDTO();
	}
	
}
