package com.futuro.api_iot_data.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.City;
import com.futuro.api_iot_data.models.Company;
import com.futuro.api_iot_data.models.Location;
import com.futuro.api_iot_data.models.DTOs.LocationDTO;
import com.futuro.api_iot_data.repositories.CityRepository;
import com.futuro.api_iot_data.repositories.CompanyRepository;
import com.futuro.api_iot_data.repositories.LocationRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;


/**
 * Implementación de la interfaz {@link ILocationService}
 */
@Service
public class LocationServiceImp implements ILocationService {
	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CityRepository cityRepository;

	@Override
	public ResponseServices create(LocationDTO locationDTO) {
		String locationName = locationDTO.getLocationName();
		var locationMeta = locationDTO.getLocationMeta();

		// Se valida el nombre de la locación
		if (locationName == null) {
			return ResponseServices.builder()
					.code(400)
					.message("El nombre de la locación es obligatorio")
					.modelDTO(locationDTO)
					.build();
		}
		if (locationRepository.existsByLocationName(locationName)) {
			return ResponseServices.builder()
					.code(400)
					.message("Ya existe una locación con este nombre")
					.modelDTO(locationDTO)
					.build();
		}

		// Se validan los metadatos de la locación
		if (locationMeta == null) {
			return ResponseServices.builder()
					.code(400)
					.message("Las metadatas de la locación son obligatorias")
					.modelDTO(locationDTO)
					.build();
		}

		// Se valida la compañia
		if (locationDTO.getCompanyId() == null) {
			return ResponseServices.builder()
					.code(400)
					.message("La compañía es obligatoria")
					.modelDTO(locationDTO)
					.build();
		}
		Company company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
		if (company == null) {
			return ResponseServices.builder()
					.code(400)
					.message("No se ha encontrado la compañía")
					.modelDTO(locationDTO)
					.build();
		}

		if (locationDTO.getCityId() == null) {
			return ResponseServices.builder()
					.code(400)
					.message("La ciudad es obligatoria")
					.modelDTO(locationDTO)
					.build();
		}
		City city = cityRepository.findById(locationDTO.getCityId()).orElse(null);
		if (city == null) {
			return ResponseServices.builder()
					.code(400)
					.message("No se ha encontrado la ciudad")
					.modelDTO(locationDTO)
					.build();
		}

		Location objLocation = Location.builder()
				.locationName(locationName)
				.locationMeta(locationMeta)
				.company(company)
				.city(city)
				// .isActive(locationDTO.getIsActive())
				.isActive(true) // Al crear, las compañias siempre van a estar activas
				.createdDate(new Date(System.currentTimeMillis()))
				.updateDate(new Date(System.currentTimeMillis()))
				.build();

		objLocation = locationRepository.save(objLocation);
		return ResponseServices.builder()
				.code(201)
				.message("La locación se ha creado con éxito")
				.modelDTO(this.parseLocationDataToLocationDTO(objLocation))
				.build();
	}

	@Override
	public ResponseServices update(Integer id, LocationDTO locationDTO) {
		Location objLocation = locationRepository.findById(id).orElse(null);
		if(objLocation == null){
			return ResponseServices.builder()
				.code(400)
				.message("No se ha encontrado la locación específicada")
				.modelDTO(locationDTO)
				.build();
		}

		String locationName = locationDTO.getLocationName();
		var locationMeta = locationDTO.getLocationMeta();

		// Se valida el nombre de la locación
		if (locationName != null && locationRepository.existsByLocationNameAndLocationIdNot(locationName, id)) {
			return ResponseServices.builder()
				.code(400)
				.message("Ya existe una locación con este nombre")
				.modelDTO(locationDTO)
				.build();
		}

		// Se valida la compañía
		Company company;
		if (locationDTO.getCompanyId() == null) {
			company = objLocation.getCompany();
		} else {
			company = companyRepository.findById(locationDTO.getCompanyId()).orElse(null);
			if(company == null){
				return ResponseServices.builder()
				.code(400)
				.message("No se ha encontrado la compañía")
				.modelDTO(locationDTO)
				.build();
			}
		}

		// Se valida la ciudad
		City city;

		if (locationDTO.getCityId() == null) {
			city = objLocation.getCity();
		} else {
			city = cityRepository.findById(locationDTO.getCityId()).orElse(null);
			if(city == null){
				return ResponseServices.builder()
				.code(400)
				.message("No se ha encontrado la ciudad")
				.modelDTO(locationDTO)
				.build();
			}

		}

		Boolean isActive = locationDTO.getIsActive();

		objLocation.setLocationName(locationName != null ? locationName : objLocation.getLocationName());
		objLocation.setLocationMeta(locationMeta != null ? locationMeta : objLocation.getLocationMeta());
		objLocation.setCompany(company);
		objLocation.setCity(city);
		objLocation.setIsActive(isActive != null ? isActive : objLocation.getIsActive());
		objLocation.setUpdateDate(new Date(System.currentTimeMillis()));
		objLocation = locationRepository.save(objLocation);
		return ResponseServices.builder()
				.code(200)
				.message("La locación se ha actualizado con éxito")
				.modelDTO(this.parseLocationDataToLocationDTO(objLocation))
				.build();
	}

	@Override
	public ResponseServices findAll() {
		List<Location> locations = locationRepository.findAll();
		if (locations.isEmpty()) {
			return ResponseServices.builder()
				.code(200)
				.message("No se ha encontrado ninguna locación")
				.listModelDTO(new ArrayList<>())
				.build();
		}
		return ResponseServices.builder()
				.code(200)
				.message("Listado de locaciones encontradas")
				.listModelDTO(locations.stream()
				.map(this::parseLocationDataToLocationDTO).toList())
				.build();
	}

	@Override
	public ResponseServices deleteById(Integer id) {
		if (!locationRepository.existsById(id)) {
			return ResponseServices.builder()
				.code(400)
				.message("No se ha encontrado la locación indicada")
				.modelDTO(new LocationDTO())
				.build();
		}
		Location objLocation = locationRepository.findById(id).get();

		locationRepository.deleteById(id);
		if (locationRepository.existsById(id)) {
			return ResponseServices.builder()
				.code(400)
				.message("No se pudo eliminar la locación")
				.modelDTO(new LocationDTO())
				.build();
		}
		return ResponseServices.builder()
				.code(200)
				.message("La locación se ha eliminado con éxito")
				.modelDTO(this.parseLocationDataToLocationDTO(objLocation))
				.build();
	}

	@Override
	public ResponseServices findById(Integer id) {
		Location objLocation = locationRepository.findById(id).orElse(new Location());
		if (objLocation == null || objLocation.getLocationId() == null) {
			return ResponseServices.builder()
				.code(404)
				.message("No se ha encontrado la locación especificada")
				.modelDTO(new LocationDTO())
				.build();
		}

		return ResponseServices.builder()
				.code(200)
				.message("Locación encontrada")
				.modelDTO(this.parseLocationDataToLocationDTO(objLocation))
				.build();
	}

	private LocationDTO parseLocationDataToLocationDTO(Location objLocation) {
		//AdminDTO adminDTO = new AdminDTO();
		//adminDTO.setUsername(objLocation.getCompany().getAdmin().getUsername());
		return LocationDTO.builder()
				.locationId(objLocation.getLocationId())
				.locationName(objLocation.getLocationName())
				.locationMeta(objLocation.getLocationMeta())
				.companyId(objLocation.getCompany().getId())
				.cityId(objLocation.getCity().getId())
				.isActive(objLocation.getIsActive())
				.createdDate(objLocation.getCreatedDate())
				.updateDate(objLocation.getUpdateDate())
				.build();
	}

}