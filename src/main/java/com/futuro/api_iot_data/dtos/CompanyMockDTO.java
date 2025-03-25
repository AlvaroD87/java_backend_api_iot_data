package com.futuro.api_iot_data.dtos;


import java.sql.Date;

import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.models.DTOs.ITemplateDTO;

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
public class CompanyMockDTO implements ITemplateDTO {
	private Long companyId;
    private String companyName;
    private String companyApiKey;
    private Boolean isActive;
    private Date createdDate;
    private Date updateDate;
    private AdminDTO adminDTO;
}
