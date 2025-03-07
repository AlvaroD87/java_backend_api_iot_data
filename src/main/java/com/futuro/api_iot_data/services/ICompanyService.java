package com.futuro.api_iot_data.services;

import com.futuro.api_iot_data.models.DAOs.CompanyDAO;
import com.futuro.api_iot_data.services.util.ResponseServices;

public interface ICompanyService {
    ResponseServices createCompany(CompanyDAO companyDAO);
    ResponseServices getCompanyById(Integer id);
    ResponseServices updateCompany(Integer id, CompanyDAO companyDAO);
    ResponseServices deleteCompany(Integer id);
    ResponseServices getAllCompanies();
}