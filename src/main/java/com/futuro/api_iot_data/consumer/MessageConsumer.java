package com.futuro.api_iot_data.consumer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.services.ISensorDataService;

/**
 * Permite conectarse a una cola de activemq para la carga y procesamiento de datos
 * de dispositivos que no admitan el protocolo tcp/ip
 */
@Component
public class MessageConsumer {
    private ObjectMapper mapper = new ObjectMapper();
    
    @Autowired
    private ISensorDataService sensorDataService;

    /**
     * Permite obtener y procesar los datos desde la cola de mensajería
     * @param message representa el mensaje recibido
     */
    @JmsListener(destination = "grupo3_prueba1")
    public void receiveFromQueue(String message){
        try{
            Map<String,Object> datos = mapper.readValue(message, Map.class);
            // Primero extraemos el api key y los datos
            var apiKey = datos.get("api_key");
            var jsonData = datos.get("json_data");

            // Si no se envío el api key o los datos, no se guardará nada
            if(apiKey == null || jsonData == null){
                return;
            }

            // Guardamos los datos
            String apiKeyString = apiKey.toString();
            List<JsonNode> jsonNodeData = mapper.convertValue(jsonData,
                mapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));

            sensorDataService.insertData(apiKeyString,jsonNodeData);


           
        }catch(Exception e){
            System.out.println("Error al convertir el mensaje recibido a json" + e.getMessage() );
        }
    }
    
}
