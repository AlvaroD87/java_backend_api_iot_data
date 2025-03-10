package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.models.DTOs.ITemplateDTO;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;
import lombok.RequiredArgsConstructor;
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
                    .message("A company with the same name already exists")
                    .code(400)
                    .build();
        }

        String companyApiKey = UUID.randomUUID().toString();

        Company company = new Company();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setCompanyApiKey(companyApiKey);
        company.setIsActive(true);
        company.setCreatedDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        companyRepository.save(company);

        // Crear un DTO para la respuesta
        CompanyResponse response = new CompanyResponse();
        response.setCompanyName(company.getCompanyName());
        response.setCompanyApiKey(company.getCompanyApiKey());

        return ResponseServices.builder()
                .message("Company created successfully")
                .code(200)
                .modelDTO(response)
                .build();
    }

    /**
     * Obtiene una compañía por su API Key.
     *
     * @param companyApiKey API Key de la compañía.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe, devuelve un mensaje de error.
     *         - Si la compañía existe, devuelve la compañía encontrada.
     */
    @Override
    public ResponseServices getCompanyByApiKey(String companyApiKey) {
        Optional<Company> companyOptional = companyRepository.findByCompanyApiKey(companyApiKey);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Company not found with the provided API Key")
                    .code(404)
                    .build();
        }

        Company company = companyOptional.get();
        CompanyResponse response = new CompanyResponse();
        response.setCompanyName(company.getCompanyName());
        response.setCompanyApiKey(company.getCompanyApiKey());

        return ResponseServices.builder()
                .message("Company found")
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
    public ResponseServices getAllCompanies() {
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            return ResponseServices.builder()
                    .message("No companies found")
                    .code(404)
                    .build();
        }

        List<CompanyResponse> companyResponses = companies.stream()
                .map(company -> {
                    CompanyResponse response = new CompanyResponse();
                    response.setCompanyName(company.getCompanyName());
                    response.setCompanyApiKey(company.getCompanyApiKey());
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseServices.builder()
                .message("Companies found")
                .code(200)
                .listModelDTO(companyResponses)
                .build();
    }

    /**
     * Actualiza el nombre de una compañía existente.
     *
     * @param companyApiKey API Key de la compañía a actualizar.
     * @param companyDTO    DTO con la nueva información de la compañía.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe, devuelve un mensaje de error.
     *         - Si el nombre de la compañía ya existe, devuelve un mensaje de error.
     *         - Si la compañía se actualiza correctamente, devuelve un mensaje de éxito.
     */
    @Override
    public ResponseServices updateCompany(String companyApiKey, CompanyDTO companyDTO) {
        Optional<Company> companyOptional = companyRepository.findByCompanyApiKey(companyApiKey);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Company not found with the provided API Key")
                    .code(404)
                    .build();
        }

        if (companyRepository.existsByCompanyName(companyDTO.getCompanyName())) {
            return ResponseServices.builder()
                    .message("A company with the same name already exists")
                    .code(400)
                    .build();
        }

        Company company = companyOptional.get();
        company.setCompanyName(companyDTO.getCompanyName());
        company.setUpdateDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        companyRepository.save(company);

        return ResponseServices.builder()
                .message("Company updated successfully")
                .code(200)
                .build();
    }

    /**
     * Elimina una compañía por su API Key.
     *
     * @param companyApiKey API Key de la compañía a eliminar.
     * @return Respuesta con el resultado de la operación.
     *         - Si la compañía no existe, devuelve un mensaje de error.
     *         - Si la compañía se elimina correctamente, devuelve un mensaje de éxito.
     */
    @Override
    public ResponseServices deleteCompany(String companyApiKey) {
        Optional<Company> companyOptional = companyRepository.findByCompanyApiKey(companyApiKey);
        if (companyOptional.isEmpty()) {
            return ResponseServices.builder()
                    .message("Company not found with the provided API Key")
                    .code(404)
                    .build();
        }

        companyRepository.delete(companyOptional.get());

        return ResponseServices.builder()
                .message("Company deleted successfully")
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
}