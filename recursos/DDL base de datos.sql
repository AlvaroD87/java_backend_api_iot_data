/*
create database java_backend_api_iot_data;
DROP TABLE sensors_data;
DROP TABLE sensors;
DROP TABLE locations;
DROP TABLE cities;
DROP TABLE countries;
DROP TABLE companies;
DROP TABLE admins;
DROP TABLE last_actions;
*/

create table admins (
	admin_id		SERIAL PRIMARY KEY,
	username		VARCHAR(100) UNIQUE,
	password		TEXT,
	is_active		BOOLEAN,
	created_date	TIMESTAMP,
	update_date		TIMESTAMP,
	last_action_id	INTEGER
);

create table companies(
	company_id		SERIAL PRIMARY KEY,
	company_name	VARCHAR(100),
	company_api_key	TEXT,
	admin_id		INTEGER,
	is_active		BOOLEAN,
	created_date	TIMESTAMP,
	update_date		TIMESTAMP,
	last_action_id	INTEGER
);

create table countries(
	country_id		SERIAL PRIMARY KEY,
	name			VARCHAR(100),
	is_active		BOOLEAN,
	created_date	TIMESTAMP,
	update_date		TIMESTAMP,
	last_action_id	INTEGER
);

create table cities(
	city_id			SERIAL PRIMARY KEY,
	name			VARCHAR(100),
	country_id		INTEGER,
	is_active		BOOLEAN,
	created_date	TIMESTAMP,
	update_date		TIMESTAMP,
	last_action_id	INTEGER
);

create table locations(
	location_id		SERIAL PRIMARY KEY,
	location_name	VARCHAR(100),
	location_meta	JSON,
	company_id		INTEGER,
	city_id			INTEGER,
	is_active		BOOLEAN,
	created_date	TIMESTAMP,
	update_date		TIMESTAMP,
	last_action_id	INTEGER
);

create table sensors(
	sensor_id			SERIAL PRIMARY KEY,
	sensor_name			VARCHAR(100),
	sensor_category		VARCHAR(100),
	sensor_api_key		TEXT,
	sensor_meta			JSON,
	location_id			INTEGER,
	is_active			BOOLEAN,
	created_date		TIMESTAMP,
	update_date			TIMESTAMP,
	last_action_id		INTEGER
);

create table sensors_data(
	sensor_data_id	BIGSERIAL PRIMARY KEY,
	data			JSON,
	sensor_id		INTEGER,
	is_active		BOOLEAN,
	created_date	TIMESTAMP
);

create table last_actions(
	last_action_id	SERIAL PRIMARY KEY,
	action_enum		VARCHAR(50) UNIQUE,
	action_desc		TEXT
);

ALTER TABLE admins ADD CONSTRAINT fk_admin_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE companies ADD CONSTRAINT unique_company_api_key UNIQUE (company_api_key);
ALTER TABLE companies ADD CONSTRAINT fk_companie_admin FOREIGN KEY (admin_id) REFERENCES admins(admin_id);
ALTER TABLE companies ADD CONSTRAINT fk_company_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE countries ADD CONSTRAINT fk_country_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE cities ADD CONSTRAINT fk_city_country FOREIGN KEY (country_id) REFERENCES countries(country_id);
ALTER TABLE cities ADD CONSTRAINT fk_city_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE locations ADD CONSTRAINT fk_location_city FOREIGN KEY (city_id) REFERENCES cities(city_id);
ALTER TABLE locations ADD CONSTRAINT fk_location_company FOREIGN KEY (company_id) REFERENCES companies(company_id);
ALTER TABLE locations ADD CONSTRAINT fk_location_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE sensors ADD CONSTRAINT unique_sensor_api_key UNIQUE (sensor_api_key);
ALTER TABLE sensors ADD CONSTRAINT fk_sensor_location FOREIGN KEY (location_id) REFERENCES locations(location_id);
ALTER TABLE sensors ADD CONSTRAINT fk_sensor_last_action FOREIGN KEY (last_action_id) REFERENCES last_actions(last_action_id);
ALTER TABLE sensors_data ADD CONSTRAINT fk_sensor_data_sensor FOREIGN KEY (sensor_id) REFERENCES sensors(sensor_id);

INSERT INTO last_actions (action_enum, action_desc)
	VALUES ('CREATED', 'entidad creada');
INSERT INTO last_actions (action_enum, action_desc)
	VALUES ('UPDATED', 'entidad modificada');
INSERT INTO last_actions (action_enum, action_desc)
	VALUES ('DELETED', 'entidad eliminada');
INSERT INTO last_actions (action_enum, action_desc)
	VALUES ('DELETED_BY_CASCADE', 'entidad eliminada en cascada por entidad padre');

INSERT INTO countries (name, is_active, created_date, update_date, last_action_id)
	VALUES ('Chile',True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);

INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Arica',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Putre',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Iquique',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Alto Hospicio',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Pozo Almonte',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Antofagasta',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Calama',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Tocopilla',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Mejillones',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Taltal',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Pedro de Atacama',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Copiapó',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Vallenar',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Chañaral',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Diego de Almagro',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Caldera',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Huasco',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('La Serena',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Coquimbo',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Ovalle',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Illapel',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Los Vilos',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Salamanca',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Valparaíso',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Viña del Mar',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Quilpué',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Villa Alemana',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Concón',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Antonio',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Quillota',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('La Calera',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Limache',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Los Andes',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Felipe',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Santiago',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Puente Alto',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Maipú',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('La Florida',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Bernardo',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Pudahuel',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Las Condes',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Peñalolén',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Lo Barnechea',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Rancagua',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Fernando',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Santa Cruz',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Rengo',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Pichilemu',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Talca',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Curicó',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Linares',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Cauquenes',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Constitución',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Molina',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Concepción',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Talcahuano',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('San Pedro de la Paz',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Coronel',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Lota',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Los Ángeles',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Temuco',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Angol',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Villarrica',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Puerto Montt',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Osorno',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Castro',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Punta Arenas',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Puerto Natales',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);
INSERT INTO cities (name, country_id, is_active, created_date, update_date, last_action_id)
    VALUES ('Puerto Williams',1,True,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1);