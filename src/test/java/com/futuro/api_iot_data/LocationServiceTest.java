package com.futuro.api_iot_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
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

import com.futuro.api_iot_data.dtos.CityMockDTO;
import com.futuro.api_iot_data.dtos.CompanyMockDTO;
import com.futuro.api_iot_data.dtos.LocationDTO;
import com.futuro.api_iot_data.models.AdminMock;
import com.futuro.api_iot_data.models.CityMock;
import com.futuro.api_iot_data.models.CompanyMock;
import com.futuro.api_iot_data.models.CountryMock;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.repositories.AdminMockRepository;
import com.futuro.api_iot_data.repositories.CityMockRepository;
import com.futuro.api_iot_data.repositories.CompanyMockRepository;
import com.futuro.api_iot_data.repositories.CountryMockRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.services.LocationServiceImp;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CompanyMockRepository companyRepository;

    @Mock
    private CityMockRepository cityRepository;

    @Mock
    private CountryMockRepository countryRepository;

    @Mock
    private AdminMockRepository adminRepository;

    @InjectMocks
    private LocationServiceImp locationService;

    private AdminMock admin;
    private CompanyMock company;
    private CountryMock country;
    private CityMock city;
    private LocationDTO locationDTO;
    private Location location;

    @BeforeEach
    public void setUp() {
        admin = AdminMock.builder()
                .adminId(1L)
                .username("admin")
                .password("1234")
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();

        company = new CompanyMock();
        company.setCompanyId(1L);
        company.setCompanyName("Compañía de prueba");
        company.setCompanyApiKey("4324234234");
        company.setIsActive(true);
        company.setAdmin(admin);
        company.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        company.setUpdateDate(new Timestamp(System.currentTimeMillis()));

        country = CountryMock.builder()
                .countryId(1L)
                .name("Chile")
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();

        city = CityMock.builder()
                .cityId(1L)
                .name("Ciudad de prueba")
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .country(country)
                .build();

        locationDTO = LocationDTO.builder()
                .locationId(1L)
                .locationName("Locación inicial")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .companyDTO(CompanyMockDTO.builder().companyId(company.getCompanyId()).build())
                .cityDTO(CityMockDTO.builder().cityId(city.getCityId()).build())
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();

        location = Location.builder()
                .locationId(1L)
                .locationName("Locación inicial")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .company(company)
                .city(city)
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .updateDate(new Timestamp(System.currentTimeMillis()))
                .build();
    }

    @Test
    public void testCreateLocationSuccess() {
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        when(cityRepository.findById(any(Long.class))).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        LocationDTO result = locationService.create(locationDTO);

        assertNotNull(result.getLocationId());
        assertEquals("Locación inicial", result.getLocationName());
    }

    @Test
    public void testFindAllLocations() {
        when(locationRepository.findAll()).thenReturn(Collections.singletonList(location));

        List<LocationDTO> result = locationService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Locación inicial", result.get(0).getLocationName());
    }

    @Test
    public void testFindLocationByIdSuccess() {
        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(location));

        LocationDTO foundLocation = locationService.findById(1L);

        assertNotNull(foundLocation.getLocationId());
        assertEquals("Locación inicial", foundLocation.getLocationName());
    }

    @Test
    public void testUpdateLocationSuccess() {
        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        when(cityRepository.findById(any(Long.class))).thenReturn(Optional.of(city));

        String nombreActualizado = "Nombre de locación actualizado";
        locationDTO.setLocationName(nombreActualizado);
        locationService.update(1L, locationDTO);

        LocationDTO updatedLocation = locationService.findById(1L);
        assertEquals(nombreActualizado, updatedLocation.getLocationName());
    }

    @Test
    public void testDeleteLocationByIdSuccess() {
        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.of(location));
        //doNothing().when(locationRepository).deleteById(any(Long.class));
        when(locationRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    
        locationService.deleteById(1L);
    
        assertNull(locationService.findById(1L).getLocationId());
    }
}
