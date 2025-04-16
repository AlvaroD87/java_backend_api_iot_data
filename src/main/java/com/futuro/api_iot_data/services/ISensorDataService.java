package com.futuro.api_iot_data.services;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.futuro.api_iot_data.services.util.ResponseServices;

/**
 * Interfaz que define los servicios para el manejo de datos de sensores.
 * 
 * <p>Proporciona los métodos necesarios para la inserción y consulta
 * de datos generados por dispositivos IoT.</p>
 */
public interface ISensorDataService {
	
	/**
     * Inserta datos recibidos desde un sensor en el sistema.
     * 
     * @param sensorApiKey API Key que identifica y autentica el sensor
     * @param dataList Lista de datos en formato JSON a almacenar
     * @return ResponseServices con el resultado de la operación:
     *         - Código 201 (CREATED) si la inserción fue exitosa
     *         - Código 400 (BAD_REQUEST) si la API Key es inválida
     *         - Código 422 (UNPROCESSABLE_ENTITY) para datos mal formados
     */
	ResponseServices insertData(String sensorApiKey, List<JsonNode> dataList);
	
	/**
     * Obtiene datos de sensores según parámetros de consulta.
     * 
     * @param parameters Objeto JSON con los parámetros de búsqueda
     * @return ResponseServices con los datos del sensor
     */
	ResponseServices getData(JsonNode parameters);
	
}
