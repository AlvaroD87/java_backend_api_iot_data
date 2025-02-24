package com.futuro.api_iot_data.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int company_id;

    @Column(nullable = false)
    private String company_name;

    @Column(unique = true, nullable = false)
    private String company_api_key;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    @Column(nullable = false)
    private boolean is_active;

    @Column(nullable = false)
    private LocalDateTime created_date;

    @Column(nullable = false)
    private LocalDateTime update_date;

    
    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_api_key() {
        return company_api_key;
    }

    public void setCompany_api_key(String company_api_key) {
        this.company_api_key = company_api_key;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public LocalDateTime getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDateTime created_date) {
        this.created_date = created_date;
    }

    public LocalDateTime getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(LocalDateTime update_date) {
        this.update_date = update_date;
    }
}