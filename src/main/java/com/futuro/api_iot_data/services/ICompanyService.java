package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define los servicios disponibles para la entidad Company (Compañía).
 * Contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * y para obtener la lista de todas las compañías registradas en el sistema.
 */
public interface ICompanyService {
    /**
     * Crea una nueva compañía en el sistema.
     *
     * @param companyDTO DTO con los datos de la compañía a crear
     * @param username Nombre de usuario del administrador que realiza la operación
     * @return ResponseServices con el resultado de la operación:
     *         - Código 201 (CREATED) y la compañía creada en caso de éxito
     *         - Código 400 (BAD_REQUEST) si ya existe una compañía con el mismo nombre
     *         - Código 401 (UNAUTHORIZED) si el usuario no tiene permisos
     */
	ResponseServices createCompany(String username, CompanyDTO companyDTO);

    /**
     * Obtiene una compañía por su ID y API Key de validación.
     *
     * @param id ID de la compañía a buscar
     * @param companyApiKey API Key para validación de acceso
     * @return ResponseServices con el resultado:
     *         - Código 200 (OK) y los datos de la compañía si existe
     *         - Código 404 (NOT_FOUND) si la compañía no existe
     *         - Código 403 (FORBIDDEN) si la API Key no es válida
     */
	ResponseServices getCompanyById(String username, Integer id);

    /**
     * Obtiene todas las compañías registradas en el sistema.
     *
     * @param username Nombre de usuario del administrador solicitante
     * @return ResponseServices con:
     *         - Código 200 (OK) y lista de compañías
     *         - Código 404 (NOT_FOUND) si no hay compañías registradas
     */
    ResponseServices getAllCompanies(String username);

    /**
     * Actualiza los datos de una compañía existente.
     *
     * @param id ID de la compañía a actualizar
     * @param companyDTO DTO con los nuevos datos de la compañía
     * @param companyApiKey API Key para validación de acceso
     * @return ResponseServices con el resultado:
     *         - Código 200 (OK) si la actualización fue exitosa
     *         - Código 400 (BAD_REQUEST) si el nombre ya existe
     *         - Código 404 (NOT_FOUND) si la compañía no existe
     */
    ResponseServices updateCompany(String username, Integer id, CompanyDTO companyDTO);
    
    /**
     * Elimina una compañía del sistema (eliminación lógica).
     *
     * @param id ID de la compañía a eliminar
     * @param companyApiKey API Key para validación de acceso
     * @return ResponseServices con el resultado:
     *         - Código 200 (OK) si la eliminación fue exitosa
     *         - Código 404 (NOT_FOUND) si la compañía no existe
     *         - Código 403 (FORBIDDEN) si la API Key no es válida
     */
    ResponseServices deleteCompany(String username, Integer id);
}
