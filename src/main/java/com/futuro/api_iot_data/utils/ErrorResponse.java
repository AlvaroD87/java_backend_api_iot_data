package com.futuro.api_iot_data.utils;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase que representa una respuesta de error personalizada.
 * Contiene información sobre el estado HTTP y un mensaje descriptivo del error.
 * Se utiliza para enviar respuestas de error estructuradas en APIs REST.
 */
@Getter
@Setter
@NoArgsConstructor
class ErrorResponse {
    private HttpStatus status;
    private String message;

    public ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}

