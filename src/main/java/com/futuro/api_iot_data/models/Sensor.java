package com.futuro.api_iot_data.models;

import java.sql.Timestamp;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.futuro.api_iot_data.models.DTOs.SensorDTO;

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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensors")
public class Sensor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sensorId;
	
	private String sensorName;
	
	private String sensorCategory;
	
	private String sensorApiKey;
	
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
	private Map<String, Object> sensorMeta;
	
	private Integer locationId;
	
	private Boolean isActive;
	
	private Timestamp createdDate;
	
	private Timestamp updateDate;
	
	public SensorDTO toSensorDTO() { return new SensorDTO(          
			this.sensorId,
            this.sensorName,
            this.sensorCategory,
            this.sensorApiKey,
            this.sensorMeta,
            this.locationId,
            this.isActive,
            this.createdDate,
            this.updateDate
            );
	}

}