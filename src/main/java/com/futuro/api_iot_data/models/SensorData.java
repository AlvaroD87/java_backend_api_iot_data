package com.futuro.api_iot_data.models;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

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
@Table(name = "sensors_data")
public class SensorData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sensor_data_id")
	private Integer id;
	
	@Column(columnDefinition = "jsonb")
	@JdbcTypeCode(SqlTypes.JSON)
	private JsonNode data;
	
	private Integer sensorId;
	
	private Boolean is_active;
	
	@Column(name = "created_date")
	private Instant createdEpoch;

}