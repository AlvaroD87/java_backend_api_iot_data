package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.models.DTOs.ITemplateDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.EntityChangeStatusEvent;
import com.futuro.api_iot_data.services.util.EntityModel;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
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
@RequiredArgsConstructor
public class CompanyServiceImp implements ICompanyService {

    private final CompanyRepository companyRepository;
    
    @Autowired
    private ApiKeysCacheData apiKeysCacheData;

    @Autowired
    private AdminRepository adminRepository;
    
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
    public ResponseServices createCompany(CompanyDTO companyDTO) {
        if (companyRepository.existsByCompanyName(companyDTO.getCompanyName())) {
            return ResponseServices.builder()
                    .message("Ya existe una compañía con el mismo nombre")
                    .code(400)
                    .build();
        }

        // Obtenemos el Usuario authenticado
        String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        String companyApiKey = UUID.randomUUID().toString();

        Company company = new Company();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setCompanyApiKey(companyApiKey);
        company.setAdminId(adminRepository.findByUsername(username).get().getId());
        company.setIsActive(true);
        company.setCreatedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        companyRepository.save(company);
        
        apiKeysCacheData.setNewCompanyApiKey(companyApiKey);
        
        // Crear un DTO para la respuesta
        CompanyResponse response = new CompanyResponse();
        response.setCompanyName(company.getCompanyName());
        response.setCompanyApiKey(company.getCompanyApiKey());

        return ResponseServices.builder()
                .message("Compañía creada exitosamente")
                .code(200)
                .modelDTO(response)
                .build();
    }

    /**
     * Obtiene una compañía por su ID y API Key.
     *
     * @param id          ID de la compañía a buscar.
     * @param companyApiKey API Key de la compañía.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si la compañía existe, devuelve la compañía encontrada.
     */
    @Override
    public ResponseServices getCompanyById(Integer id, String username) {
        //Optional<Company> companyOptional = companyRepository.findById(id);
    	//String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    	Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(id, username);
        
    	//if (companyOptional.isEmpty() || !companyOptional.get().getCompanyApiKey().equals(companyApiKey)) {
    	if (companyOptional.isEmpty()) {
    		return ResponseServices.builder()
                    .message("Compañía no encontrada o API Key incorrecta")
                    .code(404)
                    .build();
        }

        Company company = companyOptional.get();
        CompanyResponse response = new CompanyResponse();
        response.setCompanyName(company.getCompanyName());
        response.setCompanyApiKey(company.getCompanyApiKey());

        return ResponseServices.builder()
                .message("Compañía encontrada")
                .code(200)
                .modelDTO(response)
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
    	//String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        //List<Company> companies = companyRepository.findAllByAdminId(adminRepository.findByUsername(username).get().getId());
        List<Company> companies = companyRepository.findAllActiveByUsername(username);
        if (companies.isEmpty()) {
            return ResponseServices.builder()
                    .message("No se encontraron compañías")
                    .code(404)
                    .build();
        }
        
        // Crear una lista de CompanyNameResponse (solo nombres)
        //List<CompanyNameResponse> companyNames = companies.stream()
        List<CompanyResponse> companyNames = companies.stream()
                .map(company -> new CompanyResponse(company.getCompanyName(), company.getCompanyApiKey()))
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
     * @param companyApiKey API Key de la compañía.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si el nombre de la compañía ya existe, devuelve un mensaje de error.
     *         - Si la compañía se actualiza correctamente, devuelve un mensaje de éxito.
     */
    @Override
    public ResponseServices updateCompany(Integer id, CompanyDTO companyDTO, String username) {
        //Optional<Company> companyOptional = companyRepository.findById(id);
        Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(id, username);
        //if (companyOptional.isEmpty() || !companyOptional.get().getCompanyApiKey().equals(companyApiKey)) {
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada o API Key incorrecta")
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
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Compañía actualizada exitosamente")
                .code(200)
                .build();
    }

    /**
     * Elimina una compañía por su ID y API Key.
     *
     * @param id          ID de la compañía a eliminar.
     * @param companyApiKey API Key de la compañía.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe o el API Key no coincide, devuelve un mensaje de error.
     *         - Si la compañía se elimina correctamente, devuelve un mensaje de éxito.
     */
    @Override
    @Transactional
    public ResponseServices deleteCompany(Integer id, String username) {
    	//Optional<Company> companyOptional = companyRepository.findById(id);
        Optional<Company> companyOptional = companyRepository.findActiveByIdAndUsername(id, username);
        //if (companyOptional.isEmpty() || !companyOptional.get().getCompanyApiKey().equals(companyApiKey)) {
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Compañía no encontrada o API Key incorrecta")
                    .code(404)
                    .build();
        }

        //companyRepository.delete(companyOptional.get());
        
        companyRepository.updateIsActiveStatus(id, false);
        
        eventPublisher.publishEvent(
        		EntityChangeStatusEvent.builder()
        		.entity(EntityModel.COMPANY)
        		.entityId(id)
        		.status(false)
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
    public static class CompanyResponse implements ITemplateDTO {
        private String companyName;
        private String companyApiKey;
        
        public CompanyResponse () {}
        
        public CompanyResponse(String companyName, String companyApiKey) {
        	this.companyName = companyName;
        	this.companyApiKey = companyApiKey;
        }
        
        // Getters y Setters
        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyApiKey() {
            return companyApiKey;
        }

        public void setCompanyApiKey(String companyApiKey) {
            this.companyApiKey = companyApiKey;
        }
    }

    /**
     * Clase interna para representar solo el nombre de una compañía.
     * Implementa ITemplateDTO para ser compatible con ResponseServices.
     */
    public static class CompanyNameResponse implements ITemplateDTO {
        private String companyName;

        public CompanyNameResponse(String companyName) {
            this.companyName = companyName;
        }

        // Getter
        public String getCompanyName() {
            return companyName;
        }
    }
}
