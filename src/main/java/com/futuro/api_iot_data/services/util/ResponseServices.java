package com.futuro.api_iot_data.services.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.futuro.api_iot_data.models.DTOs.ITemplateDTO;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResponseServices {
	private Integer code;
	private String message;
	@JsonProperty("entity")
	private ITemplateDTO modelDTO;
	@JsonProperty("list_entity")
	private List<? extends ITemplateDTO> listModelDTO;
}
