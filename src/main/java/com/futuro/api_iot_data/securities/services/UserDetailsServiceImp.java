package com.futuro.api_iot_data.securities.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.futuro.api_iot_data.models.Admin;
import com.futuro.api_iot_data.repositories.AdminRepository;

/**
 * Implementación personalizada de {@link UserDetailsService} para la autenticación de administradores.
 * 
 * <p>Este servicio conecta el sistema de autenticación de Spring Security con la entidad {@link Admin}
 * de la base de datos, permitiendo la autenticación mediante credenciales almacenadas.</p>
 * 
 * <p><strong>Responsabilidades:</strong></p>
 * <ul>
 *   <li>Cargar detalles de usuario desde la base de datos</li>
 *   <li>Mapear la entidad Admin a UserDetails de Spring Security</li>
 *   <li>Manejar casos cuando el usuario no existe</li>
 * </ul>
 */
@Service
public class UserDetailsServiceImp implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepo;
	
	/**
     * Carga los detalles de un usuario administrador por su nombre de usuario.
     * 
     * @param username Nombre de usuario del administrador a buscar
     * @return UserDetails con la información necesaria para la autenticación
     * @throws UsernameNotFoundException Si no se encuentra un administrador con el username especificado
     */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Admin admin = adminRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("No existe username %s", username)));
		
		return new User(admin.getUsername(), admin.getPassword(), admin.getIs_active(), true, true, true, Collections.emptyList());
	}

}
