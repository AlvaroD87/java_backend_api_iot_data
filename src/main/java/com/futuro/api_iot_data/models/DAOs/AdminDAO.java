package com.futuro.api_iot_data.models.DAOs;

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
public class AdminDAO implements ITemplateDAO{
	
	private String username;
	private String password;

}
