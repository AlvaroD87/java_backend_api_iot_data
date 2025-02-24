package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DAOs.AdminDAO;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface IAdminService {
	
	ResponseServices create(AdminDAO newAdmin);
	
}
