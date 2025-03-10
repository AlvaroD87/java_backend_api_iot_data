package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define los servicios disponibles para la entidad Company (Compañía).
 * Contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y para obtener la lista de todas las compañías registradas en el sistema.
 */
public interface ICompanyService {
    ResponseServices createCompany(CompanyDTO companyDTO);
    ResponseServices getCompanyByApiKey(String companyApiKey);
    ResponseServices getAllCompanies();
    ResponseServices updateCompany(String companyApiKey, CompanyDTO companyDTO);
    ResponseServices deleteCompany(String companyApiKey);
}