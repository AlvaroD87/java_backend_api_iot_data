package com.futuro.api_iot_data.dtos;


import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityMockDTO {
	private Long cityId;
    private String name;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
    private CountryMockDTO countryMock;

}
