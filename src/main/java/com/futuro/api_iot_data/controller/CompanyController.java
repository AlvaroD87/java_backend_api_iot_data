package com.futuro.api_iot_data.controller;

import com.futuro.api_iot_data.model.Company;
import com.futuro.api_iot_data.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public Company createCompany(@RequestParam String companyName, @RequestParam int adminId) {
        // Aquí se debería obtener el admin desde la base de datos o el contexto de seguridad
        Admin admin = new Admin();
        admin.setAdmin_id(adminId);
        return companyService.createCompany(companyName, admin);
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @PutMapping("/{companyId}")
    public Company updateCompanyName(@PathVariable int companyId, @RequestParam String newName) {
        return companyService.updateCompanyName(companyId, newName);
    }

    @DeleteMapping("/{companyId}")
    public void deleteCompany(@PathVariable int companyId) {
        companyService.deleteCompany(companyId);
    }
}