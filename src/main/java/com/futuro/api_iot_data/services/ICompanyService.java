package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define los servicios disponibles para la entidad Company (Compañía).
 * Contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y para obtener la lista de todas las compañías registradas en el sistema.
 */
public interface ICompanyService {
    ResponseServices createCompany(String username, CompanyDTO companyDTO);
    ResponseServices getCompanyById(String username, Integer id);
    ResponseServices getAllCompanies(String username);
    ResponseServices updateCompany(String username, Integer id, CompanyDTO companyDTO);
    ResponseServices deleteCompany(String username, Integer id);
}
