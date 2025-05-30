package com.futuro.api_iot_data;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.cache.LastActionCacheData;
import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.models.DTOs.LocationDTO;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.services.LocationServiceImp;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Pruebas unitarias para el servicio de ubicaciones (LocationService).
 * 
 * <p>Verifica el comportamiento del servicio para operaciones CRUD de ubicaciones,
 * incluyendo creación, consulta, actualización y eliminación.</p>
 */
@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private CityRepository cityRepository;

    //@Mock
    //private CountryRepository countryRepository;

    //@Mock
    //private AdminRepository adminRepository;
    
    @Mock
    private ApiKeysCacheData apiKeysCacheData;
    
    @Mock
    private LastActionCacheData lastActionCacheData;
    
    @Mock
    private ApplicationEventPublisher eventPublisher;
    

    @InjectMocks
    private LocationServiceImp locationService;

    //private Admin admin;
    private Company company;
    //private Country country;
    private City city;
    private LocationDTO locationDTO;
    private Location location;
    private ObjectMapper mapper = new ObjectMapper();
    private LastAction lastAction;
    private LastAction deleledCascadeAction;

    /**
     * Configuración inicial para cada prueba.
     */
    @BeforeEach
    void setUp() {
        /*
    	admin = new Admin();
        admin.setId(1);
        admin.setUsername("admin");
        admin.setPassword("1234");
        */
    	
    	Authentication authentication = new UsernamePasswordAuthenticationToken("4324234234", null, new ArrayList<>());
    	SecurityContextHolder.getContext().setAuthentication(authentication);

        company = new Company();
        company.setId(1);
        company.setCompanyName("Compañía de prueba");
        company.setCompanyApiKey("4324234234");
        company.setIsActive(true);
        company.setCreatedOn(LocalDateTime.now());
        company.setUpdatedOn(LocalDateTime.now());

        /*
        country = Country.builder()
                .id(1)
                .name("Chile")
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
        */
        
        city = City.builder()
                .id(1)
                .name("Ciudad de prueba")
                .country(null)
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
                .id(1)
                .locationName("Locación inicial")
                .locationMeta(jsonMeta)
                .company(company)
                .city(city)
                .build();
        
        lastAction = new LastAction();
        lastAction.setId(1);
        lastAction.setActionEnum("CREATED");
        
        deleledCascadeAction = new LastAction();
        deleledCascadeAction.setId(2);
        deleledCascadeAction.setActionEnum("DELETED_BY_CASCADE");
    }

    /**
     * Prueba la creación exitosa de una ubicación.
     */
    @Test
    void testCreateLocationSuccess() {
        when(companyRepository.findByCompanyApiKey("4324234234")).thenReturn(Optional.of(company));
    	//when(companyRepository.findById(any(Integer.class))).thenReturn(Optional.of(company));
        // when(cityRepository.findById(any(Long.class))).thenReturn(Optional.of(city));
        when(cityRepository.findById(any(Integer.class))).thenReturn(Optional.of(city));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(lastActionCacheData.getLastAction("CREATED")).thenReturn(lastAction);

        ResponseServices response = locationService.create(locationDTO, "4324234234");
        assertNotNull(response.getModelDTO());
        LocationDTO modelDTO = (LocationDTO) response.getModelDTO();

        assertNotNull(modelDTO.getLocationId());
        assertEquals("Locación inicial", modelDTO.getLocationName());
    }

    /**
     * Prueba la obtención de todas las ubicaciones.
     */
    @Test
    void testFindAllLocations() {
        when(locationRepository.findAllActiveByCompanyApiKey("4324234234")).thenReturn(Collections.singletonList(location));
    	//when(locationRepository.findAll()).thenReturn(Collections.singletonList(location));

        ResponseServices response  = locationService.findAll("4324234234");
        List<LocationDTO> result = response.getListModelDTO()
        		.stream()
        		.map(dto -> (LocationDTO) dto)
        		.collect(Collectors.toList());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Locación inicial", result.get(0).getLocationName());
    }

    /**
     * Prueba la búsqueda exitosa de una ubicación por ID.
     */
    @Test
    void testFindLocationByIdSuccess() {
        //when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
    	when(locationRepository.findActiveByIdAndCompanyApiKey(1, "4324234234")).thenReturn(Optional.of(location));

        ResponseServices response = locationService.findById("4324234234", 1);
        assertNotNull(response.getModelDTO());
        LocationDTO foundLocation = (LocationDTO) response.getModelDTO();

        assertNotNull(foundLocation.getLocationId());
        assertEquals("Locación inicial", foundLocation.getLocationName());
    }

     /**
     * Prueba la actualización exitosa de una ubicación.
     */
    @Test
    void testUpdateLocationSuccess() {
    	/*
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(companyRepository.findById(any(Integer.class))).thenReturn(Optional.of(company));
        */
    	when(locationRepository.findActiveByIdAndCompanyApiKey(1, "4324234234")).thenReturn(Optional.of(location));
    	when(locationRepository.save(any(Location.class))).thenReturn(location);
        when(cityRepository.findById(any())).thenReturn(Optional.of(city));
        when(lastActionCacheData.getLastAction("UPDATED")).thenReturn(lastAction);
        

        String nombreActualizado = "La locación se ha actualizado con éxito";
        locationDTO.setLocationName(nombreActualizado);
        //locationService.update("4324234234", 1, locationDTO);

        ResponseServices response = locationService.update("4324234234",1, locationDTO);
        
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(nombreActualizado, response.getMessage());
        //LocationDTO updatedLocation = (LocationDTO) response.getModelDTO();
        //assertEquals(nombreActualizado, updatedLocation.getLocationName());
    }

    /**
     * Prueba la eliminación exitosa de una ubicación.
     */
    @Test
    void testDeleteLocationByIdSuccess() {
    	//when(locationRepository.existsById(1)).thenReturn(true);
    	when(locationRepository.existsByIdAndCompanyCompanyApiKey(1, "4324234234")).thenReturn(true);
    	when(locationRepository.findById(1)).thenReturn(Optional.of(location));
    	when(locationRepository.save(any(Location.class))).thenReturn(location);
    	when(lastActionCacheData.getLastAction("DELETED")).thenReturn(lastAction);
    	when(lastActionCacheData.getLastAction("DELETED_BY_CASCADE")).thenReturn(deleledCascadeAction);
    	when(locationRepository.findAllSensorIdByLocationId(1)).thenReturn(List.of("sensor123"));
    	
    	ResponseServices response = locationService.deleteById("4324234234",1);
    	
    	assertNotNull(response);
    	assertEquals(200, response.getCode());
    	assertEquals("La locación se ha eliminado con éxito", response.getMessage());
    	
    	/*
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.of(location));
        when(locationRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
    
        locationService.deleteById("4324234234", 1);
        ResponseServices response = locationService.findById("4324234234", 1);
        assertNotNull(response.getModelDTO());
        LocationDTO deletedLocation = (LocationDTO) response.getModelDTO();
    
        assertNull(deletedLocation.getLocationId());
        */
    }
}
