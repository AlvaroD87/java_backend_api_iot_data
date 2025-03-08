package com.futuro.api_iot_data.models;

import java.sql.Date;

import com.futuro.api_iot_data.models.DTOs.CountryDTO;

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
	
	private Boolean is_active;
	
	@Column(name = "created_date")
	private Date created_in;
	
	@Column(name = "update_date")
	private Date updated_in;

	public CountryDTO toCountryDTO() { return new CountryDTO(this); }
}
