package com.futuro.api_iot_data.models.DTOs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.futuro.api_iot_data.models.Country;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY) // JsonInclude.Include.NON_NULL
public class CountryDTO implements ITemplateDTO {

	private String name;
	
	public CountryDTO(Country country) {
		this.settingByCountry(country);
	}
	
	public void settingByCountry(Country country) {
		this.name = country.getName();
	}
	
}
