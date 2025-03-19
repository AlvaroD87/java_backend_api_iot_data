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
    ResponseServices getCompanyById(Integer id, String companyApiKey);
    ResponseServices getAllCompanies();
    ResponseServices updateCompany(Integer id, CompanyDTO companyDTO, String companyApiKey);
    ResponseServices deleteCompany(Integer id, String companyApiKey);
}
