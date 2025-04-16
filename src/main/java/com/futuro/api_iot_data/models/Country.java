package com.futuro.api_iot_data.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "countries")
public class Country {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
	private Integer id;
	
	private String name;
	
	@Column(name = "is_active")
	@Builder.Default
	private Boolean isActive = true;
	
	@Column(name = "created_date")
	@Builder.Default
	private LocalDateTime createdOn = LocalDateTime.now();
	
	@Column(name = "update_date")
	@Builder.Default
	private LocalDateTime updatedOn = LocalDateTime.now();

}
