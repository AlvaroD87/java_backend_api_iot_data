package com.futuro.api_iot_data.models.DTOs;

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
public class CityDTO implements ITemplateDTO {

	private String name;
	private Country country;
	
}
