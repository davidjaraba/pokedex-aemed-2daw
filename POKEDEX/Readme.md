# POKEDEX
![pokedex logo](https://camo.githubusercontent.com/7f1f1e69bef239378a28e8aca7d1d7bd0890d37a7871d01135e2d044da6e2157/68747470733a2f2f692e696d6775722e636f6d2f415975745a4f462e706e67)

Es un aplicación que lee y almacena en una base de datos H2 una serie de pokemons leídos de un fichero .json, también permite obtener una serie de datos procesando lo obtenido del JSON.

## Autores

- Jorge Benavente Liétor
- David Jaraba Pascual

## Requisitos
- Java 17 o superior
- Gradle
- Fichero JSON con nombre `pokemon.json` en la carpeta data que contenga la información de los pokemons.
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

## Arquitectura seguida en el código
El proyecto esta estructurado en varias clases separadas con tal de definir el funcionamiento distinto de cada una. Por tanto las clases importantes del proyecto son:

La clase [DatabaseManager](src/main/java/dev/db/DatabaseManager.java) tenemos varios metodos importantes:
- `prepareStatement`: Se encarga de preparar una sentencia SQL para ser ejecutada.
- `executeQuery`: Se encarga de ejecutar una sentencia SQL que devuelve un conjunto de resultados.
- `executeUpdate`: Se encarga de ejecutar una sentencia SQL que no devuelve un conjunto de resultados.
  ![database manager code](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/databasemanager.png)

La clase [PokemonController](src/main/java/dev/controllers/PokemonController.java) alberga los métodos donde se reciben los datos, se procesan y se devuelven, se ayuda del [PokemonService](src/main/java/dev/services/PokemonService.java) para obtener los datos de la BD y poder transformarlos.
Un ejemplo de esto sería el método `groupedByType` que en base a lo que obtiene del servicio lo transforma y devuelve.
![controller code](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/controller.png)

La clase [PokemonService](src/main/java/dev/services/PokemonService.java) alberga los métodos necesarios para interactuar con la BD ayudándose del [DatabaseManager](src/main/java/dev/db/DatabaseManager.java), además carga los pokemons del fichero `pokemon.json`.
![service code](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/service1.png)
![service code](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/service2.png)
![service code](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/service3.png)

También se hace uso de la clase [PokemonUtils](src/main/java/dev/utils/PokemonUtils.java) para poder leer el JSON y convertirlo a un objeto POJO.

## Ejecución
El código deberia de ejecutarse sin problema
![runningcode](https://github.com/velcas/pokedex-aemed-2daw/tree/feature/aemet-readme/POKEDEX/images/run.png)
