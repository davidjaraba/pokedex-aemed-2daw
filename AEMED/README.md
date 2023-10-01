# PRÁCTICA 01 - AEMET

## Autores

- David Jaraba Pascual
- Jorge Benavente Liétor

## Requisitos

- Java 17 o superior
- Gradle (si quieres compilar el proyecto)
- Ficheros de datos de AEMET en la carpeta `data` con formato de nombre `AemetYYYYmmDD.csv`

## Arquitectura seguida en el código

En este proyecto se ha seguido una arquitectura de capas con el objetivo de separar las distintas responsabilidades de
la aplicación. En concreto, se ha seguido una arquitectura de 3 capas: capa de datos (en los
paquete `dev.database`, `dev.repository` y `dev.database.models`),
capa de lógica de negocio (en el paquete `dev.services`) y capa de presentación (`dev.controllers`).