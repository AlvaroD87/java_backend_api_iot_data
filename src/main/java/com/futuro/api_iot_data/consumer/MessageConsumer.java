package com.futuro.api_iot_data.consumer;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.futuro.api_iot_data.services.ISensorDataService;

import jakarta.jms.BytesMessage;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

/**
 * Permite conectarse a una cola de activemq para la carga y procesamiento de
 * datos
 * de dispositivos que no admitan el protocolo tcp/ip
 */
@Component
public class MessageConsumer {
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ISensorDataService sensorDataService;

    /**
     * Permite obtener y procesar los datos desde la cola de mensajería
     * 
     * @param message representa el mensaje recibido
     */
    @JmsListener(destination = "${activemq.topico}", containerFactory = "jmsTopicListenerFactory")
    public void receiveFromQueue(Message message) {

        try {
            String contenidoMensaje = null;
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                contenidoMensaje = textMessage.getText();
            } else if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                // Procesar el mensaje de bytes
                contenidoMensaje = this.leerBytesMessage(bytesMessage);
            } else {
                System.out.println("Tipo de mensaje no manejado: " + message.getClass().getName());
            }
            if (contenidoMensaje != null) {
                Map<String, Object> datos = mapper.readValue(contenidoMensaje, Map.class);
                // Primero extraemos el api key y los datos
                var apiKey = datos.get("api_key");
                var jsonData = datos.get("json_data");

                // Si no se envío el api key o los datos, no se guardará nada
                if (apiKey == null || jsonData == null) {
                    return;
                }

                // Guardamos los datos
                String apiKeyString = apiKey.toString();
                List<JsonNode> jsonNodeData = mapper.convertValue(jsonData,
                        mapper.getTypeFactory().constructCollectionType(List.class, JsonNode.class));
                System.out.println(apiKeyString);
                System.out.println(jsonNodeData);
                sensorDataService.insertData(apiKeyString, jsonNodeData);
            }

        } catch (Exception e) {
            System.out.println("Error al convertir el mensaje recibido a json" + e.getMessage());
        }
    }

    private String leerBytesMessage(BytesMessage bytesMessage) throws Exception {
        bytesMessage.reset(); // Importantísimo: resetea la posición de lectura
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = bytesMessage.readBytes(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }

}
