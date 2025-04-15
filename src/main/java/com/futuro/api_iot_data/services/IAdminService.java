package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define el contrato para los servicios de administración de usuarios Admin.
 * 
 * <p>Establece las operaciones básicas para la gestión de administradores en el sistema,
 * utilizando DTOs para transferencia de datos y respuestas estandarizadas.</p>
 */
public interface IAdminService {
	
	/**
     * Crea un nuevo administrador en el sistema.
     * 
     * @param newAdmin Objeto AdminDTO con los datos del nuevo administrador
     * @return ResponseServices con el resultado de la operación que incluye:
     *         - Código de estado (201 creado, 400 error de validación, etc.)
     *         - Mensaje descriptivo del resultado
     *         - El AdminDTO creado (en caso de éxito)
     */
	ResponseServices create(AdminDTO newAdmin);
	
}
