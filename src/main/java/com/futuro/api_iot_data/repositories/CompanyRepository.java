package com.futuro.api_iot_data.repositories;

import com.futuro.api_iot_data.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
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
    
    @Query(value = "select * from companies where admin_id = ?1", nativeQuery = true)
    List<Company> findAllByAdminId(Integer adminId);
    
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
}