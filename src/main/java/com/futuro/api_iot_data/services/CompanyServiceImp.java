package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.EntityChangeStatusEvent;
import com.futuro.api_iot_data.services.util.EntityModel;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementación del servicio para la entidad Company.
 * Contiene la lógica de negocio para las operaciones CRUD de compañías,
 * incluyendo la creación, actualización, eliminación y consulta de compañías.
 */
@Service
public class CompanyServiceImp implements ICompanyService {

	@Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private ApiKeysCacheData apiKeysCacheData;

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
	private LastActionCacheData lastActionCacheData;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    /**
     * Crea una nueva compañía en el sistema.
     *
     * @param companyDTO DTO con el nombre de la compañía a crear.
     * @return Respuesta con el resultado de la operación.
     *         - Si el nombre de la compañía ya existe, devuelve un mensaje de error.
     *         - Si la compañía se crea correctamente, devuelve la compañía creada y su API Key.
     */
    @Override
    public ResponseServices createCompany(String username, CompanyDTO companyDTO) {
        if (companyRepository.existsByCompanyName(companyDTO.getCompanyName())) {
            return ResponseServices.builder()
                    .message("Ya existe una compañía con el mismo nombre")
                    .code(400)
                    .build();
        }

        String companyApiKey = UUID.randomUUID().toString();

        Company company = new Company();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setCompanyApiKey(companyApiKey);
        company.setAdmin(adminRepository.findByUsername(username).get());
        company.setLastAction(lastActionCacheData.getLastAction("CREATED"));
        
        companyRepository.save(company);
        
        apiKeysCacheData.setNewCompanyApiKey(companyApiKey);

        return ResponseServices.builder()
                .message("Compañía creada exitosamente")
                .code(200)
                .modelDTO(companyToDTO(company))
                .build();
    }

    /**
     * Obtiene una compañía por su ID.
     *
     * @param id          ID de la compañía a buscar.
     * @param username username del usuario autenticado.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si la compañía existe, devuelve la compañía encontrada.
     */
    @Override
    public ResponseServices getCompanyById(String username, Integer id) {
        Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(username, id);
        
    	if (companyOptional.isEmpty()) {
    		return ResponseServices.builder()
                    .message("Compañía no encontrada")
                    .code(404)
                    .build();
        }

        Company company = companyOptional.get();

        return ResponseServices.builder()
                .message("Compañía encontrada")
                .code(200)
                .modelDTO(companyToDTO(company))
                .build();
    }

    /**
     * Obtiene todas las compañías registradas en el sistema.
     *
     * @return Respuesta con la lista de todas las compañías.
     *         - Si no hay compañías registradas, devuelve un mensaje indicando que no hay compañías.
     */
    @Override
    public ResponseServices getAllCompanies(String username) {
    	List<Company> companies = companyRepository.findAllActiveByUsername(username);
        if (companies.isEmpty()) {
            return ResponseServices.builder()
                    .message("No se encontraron compañías")
                    .code(404)
                    .build();
        }
        
        List<CompanyDTO> companyNames = companies.stream()
                .map(company -> companyToDTO(company))
                .collect(Collectors.toList());

        return ResponseServices.builder()
                .message("Compañías encontradas")
                .code(200)
                .listModelDTO(companyNames)
                .build();
    }

    /**
     * Actualiza el nombre de una compañía existente.
     *
     * @param id          ID de la compañía a actualizar.
     * @param companyDTO  DTO con la nueva información de la compañía.
     * @param username Username del usuario autenticado.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si el nombre de la compañía ya existe, devuelve un mensaje de error.
     *         - Si la compañía se actualiza correctamente, devuelve un mensaje de éxito.
     */
    @Override
    public ResponseServices updateCompany(String username, Integer id, CompanyDTO companyDTO) {
        Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(username, id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada")
                    .code(404)
                    .build();
        }

        if (companyRepository.existsByCompanyName(companyDTO.getCompanyName())) {
            return ResponseServices.builder()
                    .message("Ya existe una compañía con el mismo nombre")
                    .code(400)
                    .build();
        }

        Company company = companyOptional.get();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setUpdatedOn(LocalDateTime.now());
        company.setLastAction(lastActionCacheData.getLastAction("UPDATED"));

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Compañía actualizada exitosamente")
                .code(200)
                .build();
    }

    /**
     * Elimina una compañía por su ID.
     *
     * @param id          ID de la compañía a eliminar.
     * @param username username del usuario autenticado.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si la compañía se elimina correctamente, devuelve un mensaje de éxito.
     */
    @Override
    @Transactional
    public ResponseServices deleteCompany(String username, Integer id) {
    	Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(username, id);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada")
                    .code(404)
                    .build();
        }

        Company company = companyOptional.get();
        
        company.setIsActive(false);
        company.setLastAction(lastActionCacheData.getLastAction("DELETED"));
        company.setUpdatedOn(LocalDateTime.now());
        
        companyRepository.save(company);
        
        eventPublisher.publishEvent(
        		EntityChangeStatusEvent.builder()
        		.entity(EntityModel.COMPANY)
        		.entityId(id)
        		.status(false)
        		.lastAction(lastActionCacheData.getLastAction("DELETED_BY_CASCADE"))
        		.build()
        	);
        
        apiKeysCacheData.deleteCompanyApiKey(companyOptional.get().getCompanyApiKey());

        return ResponseServices.builder()
                .message("Compañía eliminada exitosamente")
                .code(200)
                .build();
    }

    /**
     * Clase interna para representar la respuesta de una compañía.
     * Implementa ITemplateDTO para ser compatible con ResponseServices.
     */
    private CompanyDTO companyToDTO(Company company) {
    	return CompanyDTO.builder()
    			.companyId(company.getId())
    			.companyName(company.getCompanyName())
    			.companyApiKey(company.getCompanyApiKey())
    			.build();
    }

}
