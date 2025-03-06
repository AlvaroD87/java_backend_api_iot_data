package com.futuro.api_iot_data.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.dtos.CityMockDTO;
import com.futuro.api_iot_data.dtos.CompanyMockDTO;
import com.futuro.api_iot_data.dtos.CountryMockDTO;
import com.futuro.api_iot_data.dtos.LocationDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.dtos.AdminMockDTO;
import com.futuro.api_iot_data.models.CityMock;
import com.futuro.api_iot_data.models.CompanyMock;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.repositories.CityMockRepository;
import com.futuro.api_iot_data.repositories.CompanyMockRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.utils.BadRequestInputException;
import com.futuro.api_iot_data.utils.ResourceNotFoundException;

/**
 * Implementación de la interfaz {@link LocationService}
 */
@Service
public class LocationServiceImp implements LocationService {
	@Autowired
	private LocationRepository locationRepository;
	
	@Autowired
	private CompanyMockRepository companyMockRepository;
	
	@Autowired 
	private CityMockRepository cityMockRepository;
	

	@Override
	public LocationDTO create(LocationDTO locationDTO) {
		String locationName = locationDTO.getLocationName();
		var locationMeta  = locationDTO.getLocationMeta();
		
		// Se valida el nombre de la locación
		if(locationName == null) {
			throw new BadRequestInputException("El nombre de la locación es obligatorio");
		}
		if(locationRepository.existsByLocationName(locationName)) {
			throw new BadRequestInputException("Ya existe una locación con este nombre");
		}
		
		// Se validan los metadatos de la locación
		if(locationMeta == null) {
			throw new BadRequestInputException("Las metadatas de la locación son obligatorias");
		}
		
		// Se valida la compañia
		if(locationDTO.getCompanyDTO() == null || locationDTO.getCompanyDTO().getCompanyId() == null) {
			throw new BadRequestInputException("La compañía es obligatoria");
		}
		CompanyMock company = companyMockRepository.findById(locationDTO.getCompanyDTO().getCompanyId())
	            .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la compañia"));
		
		// Se valida la ciudad
		if(locationDTO.getCityDTO() == null || locationDTO.getCityDTO().getCityId() == null ) {
			throw new BadRequestInputException("La ciudad es obligatoria");
		}
		CityMock city = cityMockRepository.findById(locationDTO.getCityDTO().getCityId())
	            .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la ciudad"));
		

		
	        

	        Location objLocation = Location.builder()
	            .locationName(locationName)
	            .locationMeta(locationMeta)
	            .company(company)
	            .city(city)
	            //.isActive(locationDTO.getIsActive())
	            .isActive(true) // Al crear, las compañias siempre van a estar activas
	            .createdDate(new Timestamp(System.currentTimeMillis()))
	            .updateDate(new Timestamp(System.currentTimeMillis()))
	            .build();

	        objLocation = locationRepository.save(objLocation);
	        return this.parseLocationDataToLocationDTO(objLocation);
	}

	@Override
	public LocationDTO update(Long id, LocationDTO locationDTO) {
		Location objLocation = locationRepository.findById(id)
	            .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la locación"));
		
		
		String locationName = locationDTO.getLocationName();
		var locationMeta  = locationDTO.getLocationMeta();
		
		// Se valida el nombre de la locación
		if(locationName != null && locationRepository.existsByLocationNameAndLocationIdNot(locationName, id)) {
			throw new BadRequestInputException("Ya existe una locación con este nombre");
		}
		
		
		// Se valida la compañía
		CompanyMock company;
		if(locationDTO.getCompanyDTO() == null || locationDTO.getCompanyDTO().getCompanyId() == null) {
			company = objLocation.getCompany();
		}else {
			company = companyMockRepository.findById(locationDTO.getCompanyDTO().getCompanyId())
		            .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la compañia"));
		}
		
		
		// Se valida la ciudad
		CityMock city;
		if(locationDTO.getCityDTO() == null || locationDTO.getCityDTO().getCityId() == null) {
			city = objLocation.getCity();
		}else {
			city = cityMockRepository.findById(locationDTO.getCityDTO().getCityId())
		            .orElseThrow(() -> new ResourceNotFoundException("No se ha encontrado la ciudad"));
			
		}
		
		
		Boolean isActive = locationDTO.getIsActive();
	        

		objLocation.setLocationName(locationName != null ? locationName: objLocation.getLocationName());
		objLocation.setLocationMeta(locationMeta != null? locationMeta: objLocation.getLocationMeta());
		objLocation.setCompany(company);
		objLocation.setCity(city);
		objLocation.setIsActive(isActive != null? isActive: objLocation.getIsActive());
		objLocation.setUpdateDate(new Timestamp(System.currentTimeMillis()));
		objLocation = locationRepository.save(objLocation);
	    return this.parseLocationDataToLocationDTO(objLocation);
	}

	@Override
	public List<LocationDTO> findAll() {
		List<Location> locations = locationRepository.findAll();
		if(locations == null) {
			return new ArrayList<LocationDTO>();
		}
		
		return locations.stream()
				.map((objLocation) -> {
			        return this.parseLocationDataToLocationDTO(objLocation);
			    }).toList();
	}

	@Override
	public LocationDTO deleteById(Long id) {
		if (!locationRepository.existsById(id)) {
            return new LocationDTO();
        }
		Location objLocation = locationRepository.findById(id).get();
        
        locationRepository.deleteById(id);
        if (locationRepository.existsById(id)) {
            return new LocationDTO();
        }
        return this.parseLocationDataToLocationDTO(objLocation);
	}

	@Override
	public LocationDTO findById(Long id) {
		Location objLocation = locationRepository.findById(id).orElse(new Location());
        if (objLocation == null || objLocation.getLocationId() == null) {
            return new LocationDTO();
        }
        return this.parseLocationDataToLocationDTO(objLocation);
	}
	
	private LocationDTO parseLocationDataToLocationDTO(Location objLocation) {
		return LocationDTO.builder()
		.locationId(objLocation.getLocationId())
        .locationName(objLocation.getLocationName())
        .locationMeta(objLocation.getLocationMeta())
        .companyDTO(CompanyMockDTO.builder()
            .companyId(objLocation.getCompany().getCompanyId())
            .companyName(objLocation.getCompany().getCompanyName())
            .companyApiKey(objLocation.getCompany().getCompanyApiKey())
            .adminMockDTO(AdminMockDTO.builder()
                .adminId(objLocation.getCompany().getAdmin().getAdminId())
                .username(objLocation.getCompany().getAdmin().getUsername())
                .password(objLocation.getCompany().getAdmin().getPassword())
                .isActive(objLocation.getCompany().getAdmin().getIsActive())
                .createdDate(objLocation.getCompany().getAdmin().getCreatedDate())
                .updateDate(objLocation.getCompany().getAdmin().getUpdateDate())
                .build())
            .isActive(objLocation.getCompany().getIsActive())
            .createdDate(objLocation.getCompany().getCreatedDate())
            .updateDate(objLocation.getCompany().getUpdateDate())
            .build())
        .cityDTO(CityMockDTO.builder()
        		.cityId(objLocation.getCity().getCityId())
                .name(objLocation.getCity().getName())
                .countryMock(CountryMockDTO.builder()
                		.countryId(objLocation.getCity().getCountry().getCountryId())
                		.name(objLocation.getCity().getCountry().getName())
                		.isActive(objLocation.getCity().getCountry().getIsActive())
                		.createdDate(objLocation.getCity().getCountry().getCreatedDate())
                		.updateDate(objLocation.getCity().getCountry().getUpdateDate())
                		.build())
                .isActive(objLocation.getCity().getIsActive())
                .createdDate(objLocation.getCity().getCreatedDate())
                .updateDate(objLocation.getCity().getUpdateDate())
        		.build())
        .isActive(objLocation.getIsActive())
        .createdDate(objLocation.getCreatedDate())
        .updateDate(objLocation.getUpdateDate())
        .build();
	}

}
