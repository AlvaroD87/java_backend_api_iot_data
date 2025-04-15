package com.futuro.api_iot_data.cache;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.repositories.CompanyRepository;

/**
 * Permite guardar las api keys de compañía y sensor en cache
 * para disminuir el número de consultas realizadas a la base de datos.
 */
@Component
public class ApiKeysCacheData {
	
	CompanyRepository companyRepo;
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> apiKeyList = new ConcurrentHashMap<>();

	/**
	 * Constructor que inicializa el cache con datos iniciales desde la base de datos
	 * @param companyRepo Repositorio de compañías para cargar los datos iniciales
	 */
	public ApiKeysCacheData(CompanyRepository companyRepo) {
		
		this.companyRepo = companyRepo;
		
		this.companyRepo.joinedCompanyKeySensorKey().stream()
			.forEach(r -> {
				String companyApiKey = (String) r[0];
				String sensorApiKey = (String) r[1];
				Integer sensorId = (Integer) r[2];
				
				apiKeyList.computeIfAbsent(companyApiKey, s -> new ConcurrentHashMap<String, Integer>());
				if(sensorApiKey != null && sensorId != null) apiKeyList.get(companyApiKey).put(sensorApiKey,sensorId);
			});
		
	}
	
	/**
	 * Verifica si la api key de compañía proporcionada es válida
	 * @param companyApiKey Api key a validar
	 * @return true si la clave proporcionada es válida, false en caso contrario
	 */
	public boolean isValidCompanyApiKey(String companyApiKey) {
		return companyApiKey != null ? apiKeyList.containsKey(companyApiKey) : false;
	}
	
	/**
	 * Verifica si la api key de sensor proporcionada es válida
	 * @param sensorApiKey  Api key a validar
	 * @return true si la clave proporcionada es válida, false en caso contrario
	 */
	public boolean isValidSensorApiKey(String sensorApiKey) {
		return apiKeyList.searchValues(1, s -> s.containsKey(sensorApiKey) ? true : null);
	}

	/**
	 * Obtiene el listado de ids de sensores pertenecientes a la compañía indicada
	 * @param companyApiKey Api key de la compañía a consultar
	 * @return Listado de ids de sensores encontrados
	 */
	public Set<Integer> getCompanySensorIds(String companyApiKey) {
		return new HashSet<Integer>(apiKeyList.getOrDefault(companyApiKey, new ConcurrentHashMap<String, Integer>()).values());
	}
	
	/**
	 * Obtiene el id de un sensor a partir de su api key
	 * @param sensorApiKey api key del sensor a consultar
	 * @return id del sensor encontrado
	 */
	public Integer getSensorId(String sensorApiKey) {
		return apiKeyList.searchValues(1, s -> s.get(sensorApiKey));
	}
	
	/**
	 * Agregar la api key de compañía en los datos almacenados en cache
	 * @param newApiKey Nueva api key a almacenar
	 */
	public void setNewCompanyApiKey(String newApiKey) {
		apiKeyList.putIfAbsent(newApiKey, new ConcurrentHashMap<String, Integer>());
	}
	
	/**
	 * Agregar la api key de sensor en los datos almacenados
	 * @param companyApiKey Api key de compañía asociada
	 * @param newSensorApiKey Api key de sensor a almacenar
	 * @param newSensorId Id del sensor a almacenar
	 */
	public void setNewSensorApiKey(String companyApiKey, String newSensorApiKey, Integer newSensorId) {
		apiKeyList.get(companyApiKey).put(newSensorApiKey, newSensorId);
	}
	
	/**
	 * Elimina la api key del sensor indicado desde los datos almacenados en cache
	 * @param companyApiKey Api key de la compañía asociada
	 * @param sensorApiKey api key del sensor a eliminar
	 */
	public void deleteSensorApiKey(String companyApiKey, String sensorApiKey) {
		apiKeyList.get(companyApiKey).remove(sensorApiKey);
	}
	
	/**
	 * Elimina la api key indicada de los datos almacenados en cache
	 * @param companyApiKey Api key de la compañía a eliminar.
	 */
	public void deleteCompanyApiKey(String companyApiKey) {
		apiKeyList.remove(companyApiKey);
	}
}
