package com.futuro.api_iot_data.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * Clase que representa la entidad Company (Compañía).
 * Contiene información sobre las compañías registradas en el sistema,
 * incluyendo su nombre, clave API, estado de actividad,
 * y fechas de creación y actualización.
 *
 * Esta clase está mapeada a la tabla "companies" en la base de datos.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Integer id;

    /**
     * Nombre de la compañía. No puede ser nulo y debe ser único.
     */
    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    /**
     * Clave API única de la compañía. No puede ser nula y debe ser única.
     */
    @Column(name = "company_api_key", unique = true, nullable = false)
    private String companyApiKey;

    /**
     * TO-DO.
     */
    //@Column(name = "admin_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admin_id")
	@JsonBackReference
	private Admin admin;
    
    /**
     * Estado de actividad de la compañía. No puede ser nulo.
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
	private Boolean isActive = true;

    /**
     * Fecha de creación de la compañía. No puede ser nula.
     */
    @Column(name = "created_date", nullable = false)
    @Builder.Default
	private LocalDateTime createdOn = LocalDateTime.now();

    /**
     * Fecha de última actualización de la compañía. No puede ser nula.
     */
    @Column(name = "update_date", nullable = false)
    @Builder.Default
	private LocalDateTime updatedOn = LocalDateTime.now();
    
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_action_id")
	@JsonBackReference
	private LastAction lastAction;
}