package com.futuro.api_iot_data.services.util;

import java.util.List;

import com.futuro.api_iot_data.models.DAOs.ITemplateDAO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseServices {
	private Integer code;
	private String message;
	private ITemplateDAO modelDAO;
	private List<ITemplateDAO> listModelDAO;
}
