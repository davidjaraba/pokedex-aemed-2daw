# POKEDEX
![enter image description here](https://camo.githubusercontent.com/7f1f1e69bef239378a28e8aca7d1d7bd0890d37a7871d01135e2d044da6e2157/68747470733a2f2f692e696d6775722e636f6d2f415975745a4f462e706e67)

Es un aplicación que lee y almacena en una base de datos H2 una serie de pokemons leídos de un fichero .json, también permite obtener una serie de datos procesando los obtenido del JSON.

## Requisitos
- Java 17 o superior
- Gradle
## Configuración
Implementa varias dependiencias y el plugin de lombock para funcionar.

    plugins {  
	  id("java")  
	    id("io.freefair.lombok") version "8.3"  
	}  
	  
	group = "org.example"  
	version = "1.0-SNAPSHOT"  
	  
	repositories {  
	  mavenCentral()  
	}  
	  
	dependencies {  
	  implementation("com.h2database:h2:2.1.210")  
	    implementation("org.mybatis:mybatis:3.5.13")  
	    implementation("com.google.code.gson:gson:2.10.1")  
	  
	    testImplementation(platform("org.junit:junit-bom:5.9.1"))  
	    testImplementation("org.junit.jupiter:junit-jupiter")  
	    testImplementation("org.mockito:mockito-core:5.5.0")  
	}

Obtiene los datos de conexión de la base datos del fichero **applicaction.properties**  que tiene este formato:

    db.username=  
	db.password=  
	db.filepath=data/aemet  
	db.initScript=init.sql

En el cuál también se puede indicar el fichero SQL que quieres que te cargue para inicializar la base datos.

## Arquitectura del proyecto
El proyecto esta estructurado en varias clase:

- **Controller**: usado para implementar varios métodos que procesan los datos almacenados en la BD.
- **DataBaseManager**: es la clase usada para manejar la conexión con la base de datos y realizar consultas a está misma.
- **Models**: aquí se implementan varias clases POJO para almacenar los datos obtenidos de leer el JSON de pokemons. También se implementa una clase para manejar las consultas a la base de datos.
- **Services**: se usa para implementar  ciertas funcionalidades necesarias para el funcionamiento del programa, como por ejemplo cargar el JSON de pokemons y leer los porkemons de un CSV.
