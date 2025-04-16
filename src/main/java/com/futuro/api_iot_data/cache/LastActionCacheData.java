package com.futuro.api_iot_data.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.futuro.api_iot_data.models.LastAction;
import com.futuro.api_iot_data.repositories.LastActionRepository;

/**
 * Almacena en cache las últimas acciones realizadas sobre los registros de las entidades
 */
@Component
public class LastActionCacheData {

	private LastActionRepository lastActionRepo;
	
	private ConcurrentHashMap<String, LastAction> allLastAction = new ConcurrentHashMap<>();
	
	/**
	 * Constructor principal que permite cargar el listado de acciones en memoria
	 * @param lastActionRepo Repositorio que permite el acceso a los datos de la base de datos
	 */
	public LastActionCacheData(LastActionRepository lastActionRepo) {
		this.lastActionRepo = lastActionRepo;
		
		this.lastActionRepo.findAll().stream()
		.forEach(l -> {
			allLastAction.put(l.getActionEnum(), l);
		});
	}
	
	/**
	 * Obtiene el detalle de una acción
	 * @param actionEnum Clave a utilizar para la búsqueda
	 * @return El registro de la acción asociada al filtro de búsqueda
	 */
	public LastAction getLastAction(String actionEnum) {
		return allLastAction.get(actionEnum);
	}
}
