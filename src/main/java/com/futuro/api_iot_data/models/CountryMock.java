package com.futuro.api_iot_data.models;

import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "countries")
public class CountryMock {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long countryId;

	private String name;
	private Boolean isActive;
	private Timestamp createdDate;
	private Timestamp updateDate;

	@OneToMany(mappedBy = "country")
	private List<CityMock> cities;
}
