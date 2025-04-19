package com.futuro.api_iot_data.controllers;

import com.futuro.api_iot_data.models.DTOs.CompanyDTO;
import com.futuro.api_iot_data.services.ICompanyService;
import com.futuro.api_iot_data.services.util.ResponseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para la entidad Company.
 * Expone endpoints para realizar operaciones CRUD sobre las compañías,
 * incluyendo la creación, consulta, actualización y eliminación.
 */
@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor
@Tag(name = "Company", description = "Controlador para la gestión de compañías")
public class CompanyController {

    private final ICompanyService companyService;

    /**
     * Crea una nueva compañía.
     *
     * @param companyDTO DTO con el nombre de la compañía a crear.
     * @param userAuthenticated Detalles del usuario autenticado
     * @return Respuesta con el resultado de la operación.
     */
    @PostMapping
    @Operation(summary = "Crear una compañía", description = "Crea una nueva compañía en el sistema.")
    @ApiResponse(responseCode = "201", description = "Company created successfully")
    @ApiResponse(responseCode = "400", description = "A company with the same name already exists")
    public ResponseEntity<ResponseServices> createCompany(
    		@AuthenticationPrincipal UserDetails userAuthenticated,
    		@Valid @RequestBody CompanyDTO companyDTO
    	) 
    {
        ResponseServices response = companyService.createCompany(userAuthenticated.getUsername(), companyDTO);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Obtiene una compañía por su ID y API Key.
     *
     * @param id          ID de la compañía a buscar.
     * @param userAuthenticated Username del usuario autenticado.
     * @return Respuesta con la compañía encontrada.
     */
    @GetMapping
    @Operation(summary = "Obtener una compañía por ID", description = "Obtiene la información de una compañía por su ID y API Key.")
    @ApiResponse(responseCode = "200", description = "Company found")
    @ApiResponse(responseCode = "404", description = "Company not found or API Key mismatch")
    public ResponseEntity<ResponseServices> getCompanyById(
    			@AuthenticationPrincipal UserDetails userAuthenticated,
    			@RequestParam(name = "id", required = false) Integer id 
    		) 
    {
        ResponseServices response = id == null 
        							? companyService.getAllCompanies(userAuthenticated.getUsername()) 
        							: companyService.getCompanyById(userAuthenticated.getUsername(), id);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Actualiza el nombre de una compañía existente.
     *
     * @param id          ID de la compañía a actualizar.
     * @param companyDTO  DTO con la nueva información de la compañía.
     * @param userAuthenticated Username del usuario autenticado.
     * @return Respuesta con el resultado de la operación.
     */
    @PutMapping
    @Operation(summary = "Actualizar una compañía", description = "Actualiza el nombre de una compañía existente.")
    @ApiResponse(responseCode = "200", description = "Company updated successfully")
    @ApiResponse(responseCode = "400", description = "A company with the same name already exists")
    @ApiResponse(responseCode = "404", description = "Company not found or API Key mismatch")
    public ResponseEntity<ResponseServices> updateCompany(
    			@AuthenticationPrincipal UserDetails userAuthenticated,
    			@RequestParam(name = "id", required = true) Integer id, 
    			@RequestBody CompanyDTO companyDTO
    		) 
    {
        ResponseServices response = companyService.updateCompany(userAuthenticated.getUsername(), id, companyDTO);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Elimina una compañía por su ID y API Key.
     *
     * @param id          ID de la compañía a eliminar.
     * @param userAuthenticated Username del usuario autenticado.
     * @return Respuesta con el resultado de la operación.
     */
    @DeleteMapping
    @Operation(summary = "Eliminar una compañía", description = "Elimina una compañía por su ID y API Key.")
    @ApiResponse(responseCode = "200", description = "Company deleted successfully")
    @ApiResponse(responseCode = "404", description = "Company not found or API Key mismatch")
    public ResponseEntity<ResponseServices> deleteCompany(
    		@AuthenticationPrincipal UserDetails userAuthenticated,
    		@RequestParam(name = "id", required = true) Integer id 
    		) 
    {
        ResponseServices response = companyService.deleteCompany(userAuthenticated.getUsername(), id);
        return ResponseEntity.status(response.getCode() == 200 ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(response);
    }
}