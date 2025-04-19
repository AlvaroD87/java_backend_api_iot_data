# 🚀 API IO - Grupo 3

Este proyecto corresponde al proyecto final del curso de desarrollo de aplicaciones backend en Java utilizando el framework SpringBoot.

## 📋 Prerrequisitos

- [Java JDK 21+](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
- [vscode](https://code.visualstudio.com/) o [Visual Studio](https://visualstudio.microsoft.com/es/)
- [PostgreSQL 17+](https://www.postgresql.org/)
- [maven](https://maven.apache.org/download.cgi)
- [Postman](https://www.postman.com/)
- (Opcional) [PgAdmin 4 ](https://www.pgadmin.org/download/pgadmin-4-windows/)


## 🛠️ Instalación

1.  **Clonar el repositorio**:
	```bash
	git clone https://github.com/AlvaroD87/java_backend_api_iot_data
	cd java_backend_api_iot_data
	git switch develop
	```

2. **Crear la base de datos del proyecto**
	* Usando su programa de gestión de base de datos preferida, se debe crear la base de datos del proyecto, por ejemplo: "java_backend_api_iot_data"
	* Luego se debe abrir y ejecutar el DDL del proyecto disponible en la carpeta de recursos
	* Este script creará todas las tablas necesarias y agregará los registros iniciales.

3.  **Crear el archivo application.properties según el archivo de ejemplo proporcionado**:
	```bash
    cp .\src\main\resources\application.properties.example .\src\main\resources\application.properties
    ```

4.  **Modificar los parametros de conexión de la base de datos según corresponda**
    
Se debe abrir el archivo "src\main\resources\application.properties" y modificar los parametros de conexión a la base de datos, las otras configuraciones se deben mantener tal como están.
	 

## 🔦 Ejecución del proyecto


**⚠️ Advertencia sobre el uso de JSON:**  

Copiar y pegar las estructuras de json puede romper el formato, se recomienda escribir manualmente los valores y/o validar el formato de json utilizando alguna herramienta como  [https://jsonlint.com/](https://jsonlint.com/)

1. **Levantar el proyecto**

	Para levantar el proyecto, se puede utilizar el programa vscode o Visual Studio o compilar el proyecto y ejecutarlo con los siguientes comandos:
	```bash
	mvn clean package -DskipTests
	java -jar .\target\api-iot-data-0.0.1-SNAPSHOT.jar
	```
2. **Visualizar la documentación de swagger**

	Una vez levantado el proyecto, la documentación de swagger quedará disponible en la dirección: [http://localhost:8080/doc/swagger-ui/index.html](http://localhost:8080/doc/swagger-ui/index.html)

3. **Crear un usuario administrador**

	 Para crear un usuario administrador, se debe realizar una solicitud de tipo POST a la ruta [http://localhost:8080/api/v1/admin](http://localhost:8080/api/v1/admin) con los siguientes datos:
	 ```json
    {
        "username": "admin",
        "password": "1234"
    }
    ```
    Es importante que la solicitud se realice desde la misma máquina donde está corriendo el servicio (p. ej: un vps) porque esta ruta valida que la solicitud se haga desde localhost, además la contraseña debe cumplir con los siguientes requisitos:
    - 8 caracteres mínimos.
    - Al menos una letra en mayúscula.
    - Al menos un número.
    - Al menos un carácter especial.

    Por ejemplo:
    ```bash
	curl -X POST -H "Content-Type: application/json" -d '{"username":"admin10","password":"Admin1234@"}' -i "localhost:8080/api/v1/admin"
	```



4. **Crear una compañía**

	 Para crear una compañía, se debe realizar una solicitud de tipo POST a la ruta [http://localhost:8080/api/v1/company](http://localhost:8080/api/v1/company) con los siguientes datos:
	 ```json
    {
        "companyName": "Primera compañía"
    }
    ```
	En autorización, se debe seleccionar "Autenticación básica" y proporcionar las credenciales del administrador creado anteriormente
	
5. **Visualizar listado de compañías**

	Para visualizar el listado de compañías, se debe realizar una solicitud de tipo GET a la ruta  [http://localhost:8080/api/v1/company](http://localhost:8080/api/v1/company).

6. **Visualizar una compañía en concrecto**

	Para visualizar una compañía en concreto se debe realizar una solicitud de tipo GET a la ruta [http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA](http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA). Se debe igual utilizar la autenticación básica.

7. **Editar una compañía**

Para editar una compañía, se debe realizar una solicitud de tipo PUT utilizando autenticación básica a la ruta  [http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA](http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA) con los siguientes datos:

    
    {
        "companyName": "Nombre actualizado"
    }
    
8. **Eliminar una compañía**

Para eliminar una compañía, se debe realizar una solicitud de tipo DELETE utilizando autenticación básica a la ruta  [http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA](http://localhost:8080/api/v1/company?id=AQUI_ID_COMPAÑÍA)

9. **Visualizar listado de paises**

	Para visualizar el listado de paises disponibles, se debe realizar una solicitud de tipo GET a la ruta: [http://localhost:8080/api/v1/country](http://localhost:8080/api/v1/country). En la cabecera de la solicitud, se debe proporcionar una clave "api-key" con una api key de una compañía registrada.

10. **Visualizar listado de ciudades**

	Para visualizar el listado de ciudades disponibles, se debe realizar una solicitud de tipo GET a la ruta: [http://localhost:8080/api/v1/city](http://localhost:8080/api/v1/city). En la cabecera de la solicitud, se debe proporcionar una clave "api-key" con una api key de una compañía registrada.



11. **Crear una locación**

	Para crear una nueva locación, se debe realizar una solicitud de tipo POST a la ruta [http://localhost:8080/api/v1/location](http://localhost:8080/api/v1/location). En la cabecera de la solicitud se debe agregar la siguiente clave 
	```json
	{
	"api-key": "api key de la compañía asociada"
	}
	```
	y el siguiente cuerpo:
	
	```json
	{
	  "locationName": "Primera locación",
	  "locationMeta": {
		"Propiedad 1": "Valor 1",
		"Propiedad 2": "Valor 2"
	  },
	  "cityId": 1
	}
	```
	El id de la compañía y el id de la ciudad, deben existir en sus respectivas tablas.

13. **Visualizar listado de locaciones**

	Para visualizar el listado de locaciones se debe realizar una solicitud de tipo GET a la siguiente dirección: [http://localhost:8080/api/v1/location](http://localhost:8080/api/v1/location). Se debe proporcionar de igual manera la api key de la compañía en la cabecera de la solicitud.
	
13. **Visualizar una locación en concreto**

	 Para visualizar una locación en concreto, se debe realizar una solicitud de tipo GET a la siguiente ruta: [http://localhost:8080/api/v1/location?id=AQUI_EL_ID](http://localhost:8080/api/v1/location?id=AQUÍ_EL_ID). Proporcionando la api key de la compañía asociada en la cabecera de la solicitud.
	 
14. **Actualizar los datos de una locación**

	Para actualizar los datos de una locación, se debe realizar una solicitud de tipo PUT a la ruta: [http://localhost:8080/api/v1/location?id=AQUI_EL_ID](http://localhost:8080/api/v1/location?id=AQUÍ_EL_ID) proporcionando la api key de la compañía asociada en la cabecera y enviando los datos actualizados como en el siguiente ejemplo:
	```json
    {
        "locationName": "Primera locación actualizada",
        "locationMeta": {
            "Propiedad 1": "Valor 1",
            "Propiedad 2": "Valor 2"
        },
          "cityId": 1
        }
    ```

15. **Eliminar una locación**

	Para eliminar una locación, se debe realizar una solicitud de tipo DELETE a la ruta: [http://localhost:8080/api/v1/location?id=AQUI_EL_ID](http://localhost:8080/api/v1/location?id=AQUÍ_EL_ID) proporcionando la api key de la compañía asociada en la cabecera
16. **Crear un sensor**

	Para crear un nuevo sensor, se debe realizar una solicitud de tipo POST a la ruta: [http://localhost:8080/api/v1/sensor](http://localhost:8080/api/v1/sensor). Incluyendo la siguiente cabecera de ejemplo
	```json
    {
        "api-key": "api key de la compañía asociada a la locación del sensor"
    }
    ```
	y un cuerpo de la solicitud parecido a lo siguiente:
	```json
    {
        "sensorName": "Primer sensor",
        "sensorCategory": "Cualquier valor",
        "sensorMeta": {
            "Propierdad 1": "valor 1"
        },
        "locationId": 1
    }
    ```

	El id de la locación específicada debe existir en la tabla correspondiente.

17. **Ver listado de sensores**

	Para ver el listado de sensores, se debe realizar una solicitud de tipo GET a la ruta: [http://localhost:8080/api/v1/sensor](http://localhost:8080/api/v1/sensor). Especificando la api key de la compañía asociada en la cabecera de la solicitud.

18. **Ver un sensor en concreto**

	Para ver un sensor en concreto, se debe realizar una solicitud de tipo GET a la ruta [http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR](http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR). Especificando en la cabecera el api key de la compañía asociada.

19. **Actualizar un sensor**

	Para actualizar un sensor, se debe realizar una solicitud de tipo PUT a la ruta [http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR](http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR). Especificando en la cabecera el api key de la compañía asociada. y los datos actualizados del sensor como en el siguiente ejemplo:
	```json
    {
        "sensorName": "Primer sensor actualizado",
        "sensorCategory": "Cualquier valor",
        "sensorMeta": {
            "Propierdad 1": "valor 1"
        },
        "locationId": 1
    }
    ```
20. **Eliminar un sensor**

	Para eliminar un sensor, se debe realizar una solicitud de tipo DELETE a la ruta [http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR](http://localhost:8080/api/v1/sensor?id=ID_DEL_SENSOR). Especificando el api key de la compañía asociada en la cabecera de la solicitud.

21. **Agregar sensor data mediante api**

	Para agregar sensor data mediante api, se debe realizar una solicitud de tipo POST a la ruta: [http://localhost:8080/api/v1/sensor_data](http://localhost:8080/api/v1/sensor_data). El cuerpo de la solicitud debe tener la siguiente estructura:
	```json
        {
            "api_key": "AQUÍ api key del sensor", 
            "json_data":[
                { 
                    "datetime": 1742861430,
                    "temp": 37.4,
                    "humidity": 0.5
                }
            ] 
        } 
    ```
22. **Ver listado de sensor data**

	Para ver el listado de sensor data, se debe realizar una solicitud de tipo GET a la ruta: [http://localhost:8080/api/v1/sensor_data](http://localhost:8080/api/v1/sensor_data). Se debe proporcionar la api key de la compañía asociada en la cabecera de la solicitud bajo la clave "api-key".
	Además se pueden utilizar los siguientes filtros de búsqueda:
	- ***from:*** Fecha epoch mínima
	- ***to:*** Fecha epoch máxima
	- ***sensor_id:*** Listado de sensores id separados por coma
	- ***sensor_category:*** Listado de categorías de sensores separados por coma.
	Por ejemplo: [http://localhost:8080/api/v1/sensor_data?from=1742861430](http://localhost:8080/api/v1/sensor_data?from=1742861430)

23. **Agregar sensor data mediante cola de mensajería**

	 Para probar la insersión de datos mediante la cola de mensajería, según como está configurada actualmente el proyecto, se puede realizar una solicitud de tipo POST a al siguiente topic de ActiveMq  [http://186.64.120.248:8161/api/message?destination=topic://tf-minera-01&type=topic](http://186.64.120.248:8161/api/message?destination=topic://tf-minera-01&type=topic) utilizando autenticación básica y usando las credenciales admin:admin y enviando un cuerpo de la solicitud parecido a lo siguiente:
	 ```json
    {
        "api_key": "b4d972dc-6ee5-444d-bd36-1d14d4c2cf57", 
        "json_data": [
            {"
                datetime": 1744841119, 
                "temp": 15.0, 
                "humidity": 47.0
            }, 
            {
                "datetime": 1744841119, 
                "temp": 18.8, 
                "humidity": 28.5
            }, 
            {
                "datetime": 1744841119, 
                "temp": 9.6, 
                "humidity":48.5
            }
        ]
    }
    ```
	El campo 'api_key' proporcionado debe pertenecer a un api key de sensor existente en la base de datos.




## 🔦 Otros
**Ejecución de test unitarios**
Para ejecutar los test unitarios, se puede ejecutar el siguiente comando (la ruta completa de mvn debe estar en el path de su equipo):
```bash
mvn test
```

**Generación y revisión de javadocs**
Para generar los javadocs, se pueden ejecutar el siguiente comando:
```
mvn javadoc:javadoc
```
Al ejecutar el comando anterior, se generará la documentación del proyecto en la carpeta "target\reports\apidocs". Se puede abrir el archivo "index.html" en el navegador para explorar la documentación.
