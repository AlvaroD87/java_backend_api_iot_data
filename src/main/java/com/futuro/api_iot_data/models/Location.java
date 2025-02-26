package com.futuro.api_iot_data.models;


import java.sql.Timestamp;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

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

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;




@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "locations")
public class Location {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    private String locationName;
    
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @JsonDeserialize(using = com.fasterxml.jackson.databind.deser.std.MapDeserializer.class)
    private Map<String, Object> locationMeta;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private CompanyMock company;
    //private Integer companyId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id")
    private CityMock city;
    //private Integer cityId;
    private Boolean isActive;
    private Timestamp createdDate;
    private Timestamp updateDate;
}
