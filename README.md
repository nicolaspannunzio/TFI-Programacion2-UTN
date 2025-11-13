# Sistema Usuario – Credencial de Acceso (TFI Programación 2)

Proyecto Integrador para la materia Programación 2 de la Tecnicatura Universitaria en Programación (UTN).

## 📝 Descripción del Dominio

[cite_start]Se eligió como dominio la gestión de usuarios y credenciales de acceso[cite: 306, 327]. [cite_start]Este escenario permite aplicar los conceptos clave de la materia: persistencia con JDBC, arquitectura multicapa (DAO/Service), relaciones 1-a-1 y gestión de transacciones (commit/rollback)[cite: 328].

[cite_start]El sistema modela una relación 1-a-1 unidireccional `Usuario -> CredencialAcceso`[cite: 331].

## 💻 Requisitos Técnicos

* [cite_start]**Java:** JDK 21 [cite: 12]
* [cite_start]**IDE:** Apache NetBeans 21 [cite: 385]
* [cite_start]**Base de Datos:** MySQL Server 8.0 [cite: 386]
* **Driver:** MySQL Connector/J (incluido en el proyecto).

## 🗄️ Pasos para la Base de Datos

Para levantar el entorno de base de datos:

1.  Crear una nueva base de datos (schema) en MySQL con el nombre `tpi-bd-i`.
2.  Ejecutar el script `sql/1_estructura.sql` para crear las tablas (`usuarios`, `credencialesacceso`) y sus relaciones.
3.  Ejecutar el script `sql/2_datos_prueba.sql` para cargar los datos de prueba iniciales.

## 🚀 Cómo Compilar y Ejecutar

**[PENDIENTE]**

*(Esta sección se completará al finalizar el desarrollo del Punto 5: `AppMenu`)*

1.  Asegúrese de que el archivo `DatabaseConnection.java` (en el paquete `config`) tenga las credenciales (usuario y contraseña) correctas de su servidor MySQL.
2.  Compile el proyecto.
3.  Ejecute el archivo `Main.java` (en el paquete `main`).
4.  Se iniciará el menú por consola (`AppMenu`).

## 🎥 Video Demostración

**[PENDIENTE]**

Enlace al video de la demostración: [Link a YouTube o Google Drive aquí]

## 👥 Integrantes

* Pablo Molinari
* Nicolás Olima
* Leonel Mercorelli
* Nicolás Pannunzio
