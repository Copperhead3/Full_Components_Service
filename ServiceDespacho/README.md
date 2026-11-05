# ServiceDespacho

Microservicio de despacho para FullComponents Store.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL / XAMPP
- Lombok
- RestTemplate
- Swagger / OpenAPI

## Puerto
`8085`

## Base de datos
`fullcomponents_despacho`

## Swagger
`http://localhost:8085/swagger-ui.html`

## Comunicación
El microservicio está preparado para validar el `idPedido` mediante RestTemplate:

`ServiceDespacho -> ServicePedidos :8086`

## Gateway
Posteriormente será expuesto a través del GatewaySpring en el puerto `8090`.
