package com.futuro.api_iot_data.controllers;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.services.ICompanyService;
import com.futuro.api_iot_data.services.util.ResponseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la entidad Company.
 * Expone endpoints para realizar operaciones CRUD sobre las compañías,
 * incluyendo la creación, consulta, actualización y eliminación.
 */
@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@Tag(name = "Company", description = "Controlador para la gestión de compañías")
public class CompanyController {

    private final ICompanyService companyService;

    /**
     * Crea una nueva compañía.
     *
     * @param companyDTO DTO con el nombre de la compañía a crear.
     * @return Respuesta con el resultado de la operación.
     */
    @PostMapping("/create")
    @Operation(summary = "Crear una compañía", description = "Crea una nueva compañía en el sistema.")
    @ApiResponse(responseCode = "201", description = "Company created successfully")
    @ApiResponse(responseCode = "400", description = "A company with the same name already exists")
    public ResponseEntity<ResponseServices> createCompany(@RequestBody CompanyDTO companyDTO) {
        ResponseServices response = companyService.createCompany(companyDTO);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Obtiene una compañía por su API Key.
     *
     * @param companyApiKey API Key de la compañía.
     * @return Respuesta con la compañía encontrada.
     */
    @GetMapping("/{companyApiKey}")
    @Operation(summary = "Obtener una compañía por API Key", description = "Obtiene la información de una compañía por su API Key.")
    @ApiResponse(responseCode = "200", description = "Company found")
    @ApiResponse(responseCode = "404", description = "Company not found with the provided API Key")
    public ResponseEntity<ResponseServices> getCompanyByApiKey(@PathVariable String companyApiKey) {
        ResponseServices response = companyService.getCompanyByApiKey(companyApiKey);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Obtiene todas las compañías registradas en el sistema.
     *
     * @return Respuesta con la lista de todas las compañías.
     */
    @GetMapping("/all")
    @Operation(summary = "Obtener todas las compañías", description = "Obtiene la lista de todas las compañías registradas en el sistema.")
    @ApiResponse(responseCode = "200", description = "Companies found")
    @ApiResponse(responseCode = "404", description = "No companies found")
    public ResponseEntity<ResponseServices> getAllCompanies() {
        ResponseServices response = companyService.getAllCompanies();
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Actualiza el nombre de una compañía existente.
     *
     * @param companyApiKey API Key de la compañía a actualizar.
     * @param companyDTO    DTO con la nueva información de la compañía.
     * @return Respuesta con el resultado de la operación.
     */
    @PutMapping("/update/{companyApiKey}")
    @Operation(summary = "Actualizar una compañía", description = "Actualiza el nombre de una compañía existente.")
    @ApiResponse(responseCode = "200", description = "Company updated successfully")
    @ApiResponse(responseCode = "400", description = "A company with the same name already exists")
    @ApiResponse(responseCode = "404", description = "Company not found with the provided API Key")
    public ResponseEntity<ResponseServices> updateCompany(@PathVariable String companyApiKey, @RequestBody CompanyDTO companyDTO) {
        ResponseServices response = companyService.updateCompany(companyApiKey, companyDTO);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Elimina una compañía por su API Key.
     *
     * @param companyApiKey API Key de la compañía a eliminar.
     * @return Respuesta con el resultado de la operación.
     */
    @DeleteMapping("/delete/{companyApiKey}")
    @Operation(summary = "Eliminar una compañía", description = "Elimina una compañía por su API Key.")
    @ApiResponse(responseCode = "200", description = "Company deleted successfully")
    @ApiResponse(responseCode = "404", description = "Company not found with the provided API Key")
    public ResponseEntity<ResponseServices> deleteCompany(@PathVariable String companyApiKey) {
        ResponseServices response = companyService.deleteCompany(companyApiKey);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }
}