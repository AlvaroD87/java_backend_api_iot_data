package com.futuro.api_iot_data.config;

import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

@Component
public class CustomJmsErrorHandler implements ErrorHandler {
    @Override
    public void handleError(Throwable t) {
        System.err.println("Error procesando mensaje JMS: " + t.getMessage());
        
    }
}