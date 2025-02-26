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
public class CompanyMockDTO {
	private Long companyId;
    private String companyName;
    private String companyApiKey;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
    private AdminMockDTO adminMockDTO;
    //private CityMockDTO city;
}
