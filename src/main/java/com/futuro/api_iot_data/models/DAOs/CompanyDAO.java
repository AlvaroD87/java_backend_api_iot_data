package com.futuro.api_iot_data.models.DAOs;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear o actualizar una compañía")
public class CompanyDAO {
	
	@Schema(description = "Nombre de la compañía", example = "Compañía A", required = true)
    private String companyName;
	
	@Schema(description = "ID del admin asociado a la compañía", example = "1", required = true)
    private Integer adminId;

    
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
}