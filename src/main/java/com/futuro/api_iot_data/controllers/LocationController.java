package com.futuro.api_iot_data.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futuro.api_iot_data.dtos.LocationDTO;
import com.futuro.api_iot_data.services.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationController {
	@Autowired
	private LocationService locationService;
	
	@GetMapping
	public ResponseEntity<List<LocationDTO>> findAll(){
		List<LocationDTO> locationsDTO = locationService.findAll();
		System.out.println("Tamaño");
		System.out.print(locationsDTO.size());
		if(locationsDTO.size() == 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(locationsDTO);
		}
		return ResponseEntity.status(HttpStatus.OK).body(locationsDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<LocationDTO> findById(@PathVariable Long id){
		LocationDTO locationDTO = locationService.findById(id);
		if(locationDTO.getLocationId() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(locationDTO);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(locationDTO);
	}
	
	@PostMapping
    public ResponseEntity<LocationDTO> create(@RequestBody LocationDTO locationDTO){
        LocationDTO newLocationDTO = locationService.create(locationDTO);
        if(newLocationDTO.getLocationId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(newLocationDTO);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(newLocationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> update(@PathVariable Long id,@RequestBody LocationDTO locationDTO){
        LocationDTO updatedLocationDTO = locationService.update(id,locationDTO);
        if(updatedLocationDTO.getLocationId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(updatedLocationDTO);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(updatedLocationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDTO> deleteById(@PathVariable Long id){
        LocationDTO deletedLocationDTO = locationService.deleteById(id);
        if(deletedLocationDTO.getLocationId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(deletedLocationDTO);
        }
        return ResponseEntity.status(HttpStatus.OK.value()).body(deletedLocationDTO);
    }
}
