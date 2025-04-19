package com.futuro.api_iot_data.repositories;

import com.futuro.api_iot_data.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Company.
 * Proporciona métodos para acceder y manipular los datos de las compañías en la base de datos.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    /**
     * Verifica si existe una compañía con el nombre especificado.
     *
     * @param companyName Nombre de la compañía a verificar.
     * @return Verdadero si existe, falso en caso contrario.
     */
    boolean existsByCompanyName(String companyName);

    /**
     * Busca una compañía por su API Key.
     *
     * @param companyApiKey API Key de la compañía.
     * @return La compañía encontrada, si existe.
     */
    Optional<Company> findByCompanyApiKey(String companyApiKey);
    

    /**
     * Obtiene todas las compañías asociadas a un administrador.
     * @param adminId ID del administrador
     * @return Lista de compañías del administrador
     */
    @Query(value = "select * from companies where admin_id = ?1", nativeQuery = true)
    List<Company> findAllByAdminId(Integer adminId);
    
    /**
     * Obtiene una lista de objetos con claves de compañía y sensores asociados.
     * @return Lista de arrays de objetos con [company_api_key, sensor_api_key, sensor_id]
     */
    @Query(value="""
    				select
    					c.company_api_key,
    					s.sensor_api_key,
    					s.sensor_id
    				from
    					companies c
    					left join locations l on c.company_id=l.company_id
    					left join sensors s on l.location_id=s.location_id
    				;
    			""",
    		nativeQuery = true
    		)
    List<Object[]> joinedCompanyKeySensorKey();
    
    /**
     * Actualiza el estado activo de una compañía.
     * @param companyId ID de la compañía a actualizar
     * @param newIsActive Nuevo valor para el estado activo (true/false)
     */
    @Modifying
    @Query(value = "update companies set is_active = ?2 where company_id = ?1", nativeQuery = true)
    void updateIsActiveStatus(Integer companyId, boolean newIsActive);
    
    /**
     * Busca una compañía activa por ID y nombre de usuario del administrador.
     * @param username username del usuario que está realizando la acción
     * @param companyId ID de la compañía
     * @return {@link Optional} conteniendo la Company si existe y cumple los criterios
     */
	@Query(value = "SELECT c FROM Company c JOIN c.admin a WHERE c.isActive = true AND c.id = ?2 AND a.username = ?1")
    Optional<Company> findActiveByIdAndUsername(String username, Integer companyId);
    
    /**
     * Obtiene todas las compañías activas asociadas a un administrador.
     * @param username Nombre de usuario del administrador
     * @return Lista de compañías activas del administrador
     */
    @Query(value = "SELECT c FROM Company c JOIN c.admin a WHERE c.isActive = true AND a.username = ?1")
    List<Company> findAllActiveByUsername(String username);
}