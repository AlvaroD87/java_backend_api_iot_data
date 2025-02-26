package com.futuro.api_iot_data.dtos;


import java.sql.Timestamp;
import java.util.List;

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
public class AdminMockDTO {
	private Long adminId;
    private String username;
    private String password;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
    private List<CompanyMockDTO> companies;
}
