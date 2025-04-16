package com.futuro.api_iot_data.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;

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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sensors")
public class Sensor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sensorId;
	
	private String sensorName;
	
	private String sensorCategory;
	
	private String sensorApiKey;
    
    @Column(columnDefinition = "jsonb")
	@JdbcTypeCode(SqlTypes.JSON)
	private JsonNode sensorMeta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "location_id")
	@JsonBackReference
	private Location location;
	
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