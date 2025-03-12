package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface IAdminService {
	
	ResponseServices create(AdminDTO newAdmin);
	
}
