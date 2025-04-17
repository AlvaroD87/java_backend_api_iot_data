package com.futuro.api_iot_data.services;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.cache.ApiKeysCacheData;
import com.futuro.api_iot_data.models.SensorData;
import com.futuro.api_iot_data.models.DTOs.SensorDataDTO;
import com.futuro.api_iot_data.repositories.SensorDataRepository;
import com.futuro.api_iot_data.services.util.ResponseServices;

import jakarta.transaction.Transactional;

/**
 * Implementación del servicio para manejo de datos de sensores IoT.
 * 
 * <p>Proporciona funcionalidades para:</p>
 * <ul>
 *   <li>Almacenamiento masivo de datos de sensores</li>
 *   <li>Consulta flexible con múltiples filtros</li>
 *   <li>Validación de acceso mediante API Keys</li>
 * </ul>
 */
@Service
public class SensorDataServiceImp implements ISensorDataService{

	@Autowired
	SensorDataRepository sensorDataRepo;
	
	@Autowired
	ApiKeysCacheData apiKeysCacheData;
	
	/**
     * Inserta datos recibidos desde un sensor.
     * 
     * @param sensorApiKey API Key que autentica el sensor
     * @param dataList Lista de lecturas del sensor en formato JSON
     * @return ResponseServices con:
     *         - Código 200 (éxito) y conteo de datos insertados
     *         - Código 400 (error) si la API Key es inválida
     */
	@Override
	@Transactional
	public ResponseServices insertData(String sensorApiKey, List<JsonNode> dataList) {
		
		Integer sensorId = apiKeysCacheData.getSensorId(sensorApiKey);
		Instant insertInstant = Instant.now();
		
		if (sensorId == null) {
			return ResponseServices.builder()
					.code(400)
					.message("sensor api-key invalido")
					.build();
		}
		
		Integer totalData = sensorDataRepo.saveAll(dataList.stream()
															.map(jsonData -> SensorData.builder()
																						.data(jsonData)
																						.sensorId(sensorId)
																						.is_active(true)
																						.createdEpoch(insertInstant)
																						.build()
															).toList()
												   ).size();
		return ResponseServices.builder()
				.code(200)
				.message(String.format("%d datos insertados", totalData))
				.build();
	}

	/**
     * Consulta datos de sensores con múltiples filtros.
     * 
     * @param parameters JSON con parámetros de búsqueda:
     *        - companyApiKey: API Key de la compañía (requerido)
     *        - sensorId: IDs de sensores (opcional)
     *        - sensorCategory: Categorías de sensores (opcional)
     *        - fromEpoch/toEpoch: Rango temporal (opcional)
     * @return ResponseServices con:
     *         - Código 200 y los datos encontrados
     *         - Lista vacía si no hay coincidencias
     */
	@Override
	public ResponseServices getData(JsonNode parameters) {
		
		Set<String> querySensorCategory = StreamSupport.stream(parameters.get("sensorCategory").spliterator(), false)
														.map(c -> c.asText())
														.collect(Collectors.toSet());
		
		Set<Integer> querySensorId = parameters.get("sensorId").isEmpty()
												? apiKeysCacheData.getCompanySensorIds(parameters.get("companyApiKey").asText())
												: StreamSupport.stream(parameters.get("sensorId").spliterator(), false)
																.map(i -> i.asInt())
																.collect(Collectors.toSet()); 
		
		querySensorId.retainAll(apiKeysCacheData.getCompanySensorIds(parameters.get("companyApiKey").asText()));
		
		Integer fromEpoch = parameters.get("fromEpoch").canConvertToInt() ? parameters.get("fromEpoch").asInt() : null;
		Integer toEpoch = parameters.get("toEpoch").canConvertToInt() ? parameters.get("toEpoch").asInt() : null;
		
		List<SensorData> queryResult = sensorDataRepo.findAllByParameters(querySensorId, 
																		  fromEpoch, 
																		  toEpoch, 
																		  querySensorCategory);
		
		return ResponseServices.builder()
				.code(200)
				.message(String.format("%d set de datos", queryResult.size()))
				.listModelDTO(queryResult.stream().map(d -> SensorDataDTO.builder().data(d.getData()).build()).toList())
				.build();
	}

}
