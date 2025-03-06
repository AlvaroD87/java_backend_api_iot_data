package com.futuro.api_iot_data.controllers;

import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.services.ICompanyService;
import com.futuro.api_iot_data.services.util.ResponseServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@Tag(name = "Company", description = "Operaciones relacionadas con la entidad Company")

public class CompanyController {

    @Autowired
    private ICompanyService companyService;

    @PostMapping("/create")
    @Operation(
            summary = "Crear una compañía",
            description = "Crea una nueva compañía con los datos proporcionados.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Compañía creada exitosamente",
                            content = @Content(schema = @Schema(implementation = ResponseServices.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Datos de entrada inválidos"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Ya existe una compañía con el mismo nombre"
                    )
            }
    )
    public ResponseEntity<ResponseServices> createCompany(@RequestBody CompanyDAO companyDAO) {
        ResponseServices response = companyService.createCompany(companyDAO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener una compañía por ID",
            description = "Recupera los detalles de una compañía específica por su ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Compañía encontrada",
                            content = @Content(schema = @Schema(implementation = ResponseServices.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Compañía no encontrada"
                    )
            }
    )
    public ResponseEntity<ResponseServices> getCompanyById(@PathVariable Integer id) {
        ResponseServices response = companyService.getCompanyById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Actualizar una compañía",
            description = "Actualiza los datos de una compañía existente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Compañía actualizada exitosamente",
                            content = @Content(schema = @Schema(implementation = ResponseServices.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Compañía no encontrada"
                    )
            }
    )
    public ResponseEntity<ResponseServices> updateCompany(@PathVariable Integer id, @RequestBody CompanyDAO companyDAO) {
        ResponseServices response = companyService.updateCompany(id, companyDAO);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar una compañía",
            description = "Elimina una compañía específica por su ID.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Compañía eliminada exitosamente",
                            content = @Content(schema = @Schema(implementation = ResponseServices.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Compañía no encontrada"
                    )
            }
    )
    public ResponseEntity<ResponseServices> deleteCompany(@PathVariable Integer id) {
        ResponseServices response = companyService.deleteCompany(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
    
    @GetMapping("/all")
    @Operation(
            summary = "Listar todas las compañías",
            description = "Enlista todas las compañías existentes al momento de la consulta."
            )
    public ResponseEntity<ResponseServices> getAllCompanies() {
        ResponseServices response = companyService.getAllCompanies();
        return ResponseEntity.status(response.getCode()).body(response);
    }
}