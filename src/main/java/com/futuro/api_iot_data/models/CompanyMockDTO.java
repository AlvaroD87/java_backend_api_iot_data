package com.futuro.api_iot_data.models;

import java.security.Timestamp;

import com.futuro.api_iot_data.dtos.AdminMockDTO;
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
    private AdminMockDTO admin;

}
