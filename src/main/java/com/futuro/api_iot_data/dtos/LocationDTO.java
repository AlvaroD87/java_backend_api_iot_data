package com.futuro.api_iot_data.dtos;

import java.sql.Timestamp;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

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
public class LocationDTO {
	private Long locationId;
    private String locationName;
    //private JsonNode locationMeta;
    private Map<String, Object> locationMeta;
    private CompanyMockDTO companyDTO;
    //private Integer companyId;
    private CityMockDTO cityDTO;
    //private Integer cityId;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
}
