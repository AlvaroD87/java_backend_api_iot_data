package com.futuro.api_iot_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.Country;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.models.DTOs.AdminDTO;
import com.futuro.api_iot_data.models.DTOs.LocationDTO;
import com.futuro.api_iot_data.repositories.AdminRepository;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.repositories.CountryRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.services.LocationServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private LocationServiceImp locationService;

    private Admin admin;
    private Company company;
    private Country country;
    private City city;
    private LocationDTO locationDTO;
    private Location location;
    
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("1234");

        company = new Company();
        company.setId(1);
        company.setCompanyName("Compañía de prueba");
        company.setCompanyApiKey("4324234234");
        company.setIsActive(true);
        company.setCreatedOn(LocalDateTime.now());
        company.setUpdatedOn(LocalDateTime.now());

        country = Country.builder()
                .id(1)
                .name("Chile")
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        city = City.builder()
                .id(1)
                .name("Ciudad de prueba")
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .country(country)
                .build();

        JsonNode jsonMeta;
        try {
        	jsonMeta = mapper.readTree("{\"clave uno\":\"valor 1\"}");
		} catch (JsonProcessingException e) {
			jsonMeta = null;
		}
        
        locationDTO = LocationDTO.builder()
                .locationId(1)
                .locationName("Locación inicial")
                .locationMeta(jsonMeta)
                .companyId(company.getId())
                .cityId(city.getId())
                .build();

        location = Location.builder()
                .locationId(1)
                .locationName("Locación inicial")
                .locationMeta(jsonMeta)
                .company(company)
                .city(city)
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    @Test
    void testCreateLocationSuccess() {
        when(companyRepository.findById(any(Integer.class))).thenReturn(Optional.of(company));
        // when(cityRepository.findById(any(Long.class))).thenReturn(Optional.of(city));
        when(cityRepository.findById(any(Integer.class))).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        ResponseServices response = locationService.create(locationDTO, "4324234234");
        assertNotNull(response.getModelDTO());
        LocationDTO modelDTO = (LocationDTO) response.getModelDTO();

        assertNotNull(modelDTO.getLocationId());
        assertEquals("Locación inicial", modelDTO.getLocationName());
    }

    @Test
    void testFindAllLocations() {
        when(locationRepository.findAll()).thenReturn(Collections.singletonList(location));

        ResponseServices response  = locationService.findAll("4324234234");
        
        List<LocationDTO> result = (List<LocationDTO>) response.getListModelDTO();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Locación inicial", result.get(0).getLocationName());
    }

    @Test
    void testFindLocationByIdSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));

        ResponseServices response = locationService.findById("4324234234", 1);
        assertNotNull(response.getModelDTO());
        LocationDTO foundLocation = (LocationDTO) response.getModelDTO();

        assertNotNull(foundLocation.getLocationId());
        assertEquals("Locación inicial", foundLocation.getLocationName());
    }

    @Test
    void testUpdateLocationSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(companyRepository.findById(any(Integer.class))).thenReturn(Optional.of(company));
        when(cityRepository.findById(any(Integer.class))).thenReturn(Optional.of(city));

        String nombreActualizado = "Nombre de locación actualizado";
        locationDTO.setLocationName(nombreActualizado);
        locationService.update("4324234234", 1, locationDTO);

        ResponseServices response = locationService.findById("4324234234", 1);
        assertNotNull(response.getModelDTO());
        LocationDTO updatedLocation = (LocationDTO) response.getModelDTO();
        assertEquals(nombreActualizado, updatedLocation.getLocationName());
    }

    @Test
    void testDeleteLocationByIdSuccess() {
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
    
        locationService.deleteById("4324234234", 1);
        ResponseServices response = locationService.findById("4324234234", 1);
        assertNotNull(response.getModelDTO());
        LocationDTO deletedLocation = (LocationDTO) response.getModelDTO();
    
        assertNull(deletedLocation.getLocationId());
    }
}
