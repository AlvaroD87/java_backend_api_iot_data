package com.futuro.api_iot_data.repositories;

import com.futuro.api_iot_data.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}
