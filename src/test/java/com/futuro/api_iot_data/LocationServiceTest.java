package com.futuro.api_iot_data;

import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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

import jakarta.transaction.Transactional;

@SpringBootTest
@Testcontainers
//@ActiveProfiles("test")
public class LocationServiceTest {
	
	@Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto",() -> "create-drop");
    }
	@Autowired
	AdminMockRepository adminRepository;
	
	@Autowired
	CountryMockRepository countryRepository;
	

    @Autowired
    private LocationServiceImp locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CompanyMockRepository companyRepository;

    @Autowired
    private CityMockRepository cityRepository;
    
    private AdminMock admin;
    private CompanyMock company;
    private CountryMock country;
    private CityMock city;
    private LocationDTO locationDTO;

    @BeforeEach
    public void setUp() {
    	
    	
        locationRepository.deleteAll();
        companyRepository.deleteAll();
        cityRepository.deleteAll();
        countryRepository.deleteAll();
        adminRepository.deleteAll();
        
       admin = AdminMock.builder()
    			.username("admin")
    			.password("1234")
    			.isActive(true)
    			.createdDate(new Timestamp(System.currentTimeMillis()))
    			.updateDate(new Timestamp(System.currentTimeMillis()))
    			.build();
    	adminRepository.save(admin);
    	
        company = new CompanyMock();
        company.setCompanyName("Compañía de prueba");
        company.setCompanyApiKey("4324234234");
        company.setIsActive(true);
        company.setAdmin(admin);
        company.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        company.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        company = companyRepository.save(company);
        
        
        country = CountryMock.builder()
        		.name("Chile")
        		.isActive(true)
        		.createdDate(new Timestamp(System.currentTimeMillis()))
    			.updateDate(new Timestamp(System.currentTimeMillis()))
        		.build();
        
        countryRepository.save(country);
        
        

        city =  CityMock.builder()
        		.name("Ciud de prueba")
        		.isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
    			.updateDate(new Timestamp(System.currentTimeMillis()))
    			.country(country)
        		.build();
        
        city = cityRepository.save(city);
        
        locationDTO = LocationDTO.builder()
                .locationName("Locación inicial")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .companyDTO(CompanyMockDTO.builder().companyId(company.getCompanyId()).build())
                .cityDTO(CityMockDTO.builder().cityId(city.getCityId()).build())
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
    			.updateDate(new Timestamp(System.currentTimeMillis()))
                .build();

        locationDTO = locationService.create(locationDTO);
        
        
        
    }

    @Test
    public void testCreateLocationSuccess() {
    	

        LocationDTO newLocationDTO = LocationDTO.builder()
                .locationName("Locación de prueba")
                .locationMeta(Map.of("clave uno", "valor 1"))
                .companyDTO(CompanyMockDTO.builder().companyId(company.getCompanyId()).build())
                .cityDTO(CityMockDTO.builder().cityId(city.getCityId()).build())
                .isActive(true)
                .createdDate(new Timestamp(System.currentTimeMillis()))
    			.updateDate(new Timestamp(System.currentTimeMillis()))
                .build();

        LocationDTO result = locationService.create(newLocationDTO);

        assertNotNull(result.getLocationId());
        assertEquals("Locación de prueba", result.getLocationName());
    }

    @Test
    public void testFindAllLocations() {
        
        List<LocationDTO> result = locationService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Locación inicial", result.get(0).getLocationName());
    }
    
    @Test
    public void testFindLocationByIdSuccess(){
    	Long locationId = locationDTO.getLocationId();
    	
    	LocationDTO foundLocation = locationService.findById(locationId);
    	
    	assertNotNull(foundLocation.getLocationId());
    	assertEquals(locationDTO.getLocationName(), foundLocation.getLocationName());
    	
    }
    
    @Test
    public void testUpdateLocationSuccess() {
    	Long locationId = locationDTO.getLocationId();
    	String nombreActualizado = "Nombre de locación actualizado";
    	locationDTO.setLocationName(nombreActualizado);
    	locationService.update(locationId, locationDTO);
    	
    	LocationDTO updatedLocation = locationService.findById(locationId);
    	assertEquals(nombreActualizado, updatedLocation.getLocationName());
    	
    }
    
    @Test
    public void testDeleteLocationByIsSuccess() {
    	Long locationId = locationDTO.getLocationId();
    	locationService.deleteById(locationId);
    	
    	assertNull(locationService.findById(locationId).getLocationId());
    }
    
    
}