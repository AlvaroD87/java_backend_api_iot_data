package com.futuro.api_iot_data.models;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Representa un administrador en el sistema.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "admins")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
	private Integer id;
	
	private String username;
	
	private String password;
	
	@Column(name = "is_active")
	@Builder.Default
	private Boolean isActive = true;
	
	@Column(name = "created_date")
	@Builder.Default
	private LocalDateTime createdOn = LocalDateTime.now();
	
	@Column(name = "update_date")
	@Builder.Default
	private LocalDateTime updatedOn = LocalDateTime.now();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "last_action_id")
	@JsonBackReference
	private LastAction lastAction;
}
