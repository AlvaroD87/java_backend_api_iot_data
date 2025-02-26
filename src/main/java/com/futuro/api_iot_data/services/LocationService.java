package com.futuro.api_iot_data.services;

import java.util.List;

import com.futuro.api_iot_data.dtos.LocationDTO;

public interface LocationService {
	public LocationDTO create(LocationDTO locationDTO);
	public LocationDTO update(Long id, LocationDTO locationDTO);
	public List<LocationDTO> findAll();
	public LocationDTO deleteById(Long id);
	public LocationDTO findById(Long id);
	
}
