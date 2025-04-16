package com.futuro.api_iot_data.config;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Permite capturar los errores asociados al procesamiento de datos desde una cola de mensajería
 */
@Component
public class CustomJmsErrorHandler implements ErrorHandler {
    /**
     * Permite capturar los mensajes de errores y procesarlos en caso de ser necesarios.
     * En este implementación, se mostrarán los mensajes en consola
     */
    @Override
    public void handleError(Throwable t) {
        System.err.println("Error procesando mensaje JMS: " + t.getMessage());
        
    }
}