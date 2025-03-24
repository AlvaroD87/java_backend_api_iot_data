package com.futuro.api_iot_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.futuro.api_iot_data.dtos.CompanyMockDTO;
import com.futuro.api_iot_data.dtos.LocationDTO;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.CompanyMock;
import com.futuro.api_iot_data.models.Country;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CompanyMockRepository;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.services.LocationServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CompanyMockRepository companyRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private LocationServiceImp locationService;

    private Admin admin;
    private CompanyMock company;
    private Country country;
    private City city;
    private LocationDTO locationDTO;
    private Location location;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("1234");

        company = new CompanyMock();
        company.setCompanyId(1L);
        company.setCompanyName("Compañía de prueba");
        company.setCompanyApiKey("4324234234");
        company.setIsActive(true);
        company.setAdmin(admin);
        company.setCreatedDate(new Date(System.currentTimeMillis()));
        company.setUpdateDate(new Date(System.currentTimeMillis()));

        country = Country.builder()
                .id(1)
                .name("Chile")
                .is_active(true)
                .created_in(new Date(System.currentTimeMillis()))
                .updated_in(new Date(System.currentTimeMillis()))
                .build();

        city = City.builder()
                .id(1)
                .name("Ciudad de prueba")
                .is_active(true)
                .created_in(new Date(Calendar.getInstance().getTimeInMillis()))
                .updated_in(new Date(Calendar.getInstance().getTimeInMillis()))
                .country(country)
                .build();

        locationDTO = LocationDTO.builder()
                .locationId(1)
                .locationName("Locación inicial")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .companyDTO(CompanyMockDTO.builder().companyId(company.getCompanyId()).build())
                .cityDTO(city.toCityDTO())
                .isActive(true)
                .createdDate(new Date(System.currentTimeMillis()))
                .updateDate(new Date(System.currentTimeMillis()))
                .build();

        location = Location.builder()
                .locationId(1)
                .locationName("Locación inicial")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .company(company)
                .city(city)
                .isActive(true)
                .createdDate(new Date(System.currentTimeMillis()))
                .updateDate(new Date(System.currentTimeMillis()))
                .build();
    }

    @Test
    void testCreateLocationSuccess() {
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        // when(cityRepository.findById(any(Long.class))).thenReturn(Optional.of(city));
        when(cityRepository.findByName(any(String.class))).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        ResponseServices response = locationService.create(locationDTO);
        assertNotNull(response.getModelDTO());
        LocationDTO modelDTO = (LocationDTO) response.getModelDTO();

        assertNotNull(modelDTO.getLocationId());
        assertEquals("Locación inicial", modelDTO.getLocationName());
    }

    @Test
    void testFindAllLocations() {
        when(locationRepository.findAll()).thenReturn(Collections.singletonList(location));

        ResponseServices response  = locationService.findAll();
        
        List<LocationDTO> result = (List<LocationDTO>) response.getListModelDTO();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Locación inicial", result.get(0).getLocationName());
    }

    @Test
    void testFindLocationByIdSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));

        ResponseServices response = locationService.findById(1);
        assertNotNull(response.getModelDTO());
        LocationDTO foundLocation = (LocationDTO) response.getModelDTO();

        assertNotNull(foundLocation.getLocationId());
        assertEquals("Locación inicial", foundLocation.getLocationName());
    }

    @Test
    void testUpdateLocationSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        when(cityRepository.findByName(any(String.class))).thenReturn(Optional.of(city));

        String nombreActualizado = "Nombre de locación actualizado";
        locationDTO.setLocationName(nombreActualizado);
        locationService.update(1, locationDTO);

        ResponseServices response = locationService.findById(1);
        assertNotNull(response.getModelDTO());
        LocationDTO updatedLocation = (LocationDTO) response.getModelDTO();
        assertEquals(nombreActualizado, updatedLocation.getLocationName());
    }

    @Test
    void testDeleteLocationByIdSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
    
        locationService.deleteById(1);
        ResponseServices response = locationService.findById(1);
        assertNotNull(response.getModelDTO());
        LocationDTO deletedLocation = (LocationDTO) response.getModelDTO();
    
        assertNull(deletedLocation.getLocationId());
    }
}
