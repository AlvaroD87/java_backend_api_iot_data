package com.futuro.api_iot_data.services.util;

import com.futuro.api_iot_data.models.LastAction;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa un evento de cambio de estado en una entidad del sistema.
 */
@Getter
@Setter
@Builder
public class EntityChangeStatusEvent {

	private final EntityModel entity;
	private final Integer entityId;
	private final boolean status;
	private final LastAction lastAction;

}
