package com.futuro.api_iot_data.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "companies")
@Schema(description = "Entidad que representa una compañía")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    @Schema(description = "ID único de la compañía", example = "1")
    private Integer id;

    @Column(name = "company_name", nullable = false)
    @Schema(description = "Nombre de la compañía", example = "Compañía A")
    private String companyName;

    @Column(name = "company_api_key", unique = true, nullable = false)
    @Schema(description = "API Key única de la compañía", example = "api-key-123")
    private String companyApiKey;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @Schema(description = "Admin asociado a la compañía")
    private Admin admin;

    @Column(name = "is_active", nullable = false)
    @Schema(description = "Indica si la compañía está activa", example = "true")
    private Boolean isActive;

    @Column(name = "created_date", nullable = false)
    @Schema(description = "Fecha de creación de la compañía", example = "2023-10-01T12:00:00")
    private Timestamp createdDate;

    @Column(name = "update_date", nullable = false)
    @Schema(description = "Fecha de última actualización de la compañía", example = "2023-10-01T12:00:00")
    private Timestamp updateDate;

    
    public Company() {
    }

    
    public Company(Integer id, String companyName, String companyApiKey, Admin admin, Boolean isActive, Timestamp createdDate, Timestamp updateDate) {
        this.id = id;
        this.companyName = companyName;
        this.companyApiKey = companyApiKey;
        this.admin = admin;
        this.isActive = isActive;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    
    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", companyApiKey='" + companyApiKey + '\'' +
                ", admin=" + admin +
                ", isActive=" + isActive +
                ", createdDate=" + createdDate +
                ", updateDate=" + updateDate +
                '}';
    }
}