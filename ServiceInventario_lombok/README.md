# ServiceInventario - FullComponents

Microservicio de inventario construido con la misma estructura base de ServiceSucursales.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL / XAMPP
- RestTemplate
- Swagger / OpenAPI

## Puerto
- Inventario: 8084
- Producto consumido mediante RestTemplate: 8083
- Gateway futuro: 8090

## Base de datos
`fullcomponents_inventario` (se crea automáticamente si MySQL permite crear bases).

## Swagger
`http://localhost:8084/swagger-ui.html`

## Comunicación
Inventario valida que el producto exista mediante:
`GET http://localhost:8083/api/v1/productos/buscar/{idProducto}`

## Estructura
`config -> controller -> dto -> exception -> model -> repository -> service`

La lógica de negocio se mantiene en `service`; las clases `model` solo representan datos, relaciones JPA y validaciones.


## Lombok

El proyecto incluye Lombok. Las entidades de `model` y los DTO tienen `@Getter` y `@Setter`.
Por compatibilidad con el entorno del proyecto, los getters y setters manuales existentes en las entidades se mantienen; Lombok no genera un método cuando ya existe uno con la misma firma.
