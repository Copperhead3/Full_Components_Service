# ServicePago

Microservicio de pagos de FullComponents Store.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL/XAMPP
- Lombok
- Swagger / OpenAPI
- RestTemplate configurado para futuras integraciones

## Puerto
- `8087`

## Base de datos
- `fullcomponents_pago`

## Swagger
- `http://localhost:8087/swagger-ui.html`
- `http://localhost:8087/v3/api-docs`

## Responsabilidad
Pago administra pagos, transacciones y reembolsos. Cada `Pago` conserva `idPedido` como referencia de integración con el microservicio Pedido. No existe relación JPA entre ambos microservicios.

## Endpoints principales
- `POST /api/v1/pagos`
- `GET /api/v1/pagos`
- `GET /api/v1/pagos/{idPago}`
- `GET /api/v1/pagos/pedido/{idPedido}`
- `POST /api/v1/pagos/{idPago}/iniciar`
- `POST /api/v1/pagos/{idPago}/anular`
- `GET /api/v1/pagos/{idPago}/transacciones`
- `POST /api/v1/pagos/{idPago}/reembolsos`
- `GET /api/v1/pagos/{idPago}/reembolsos`
- `POST /api/v1/pagos/{idPago}/reembolsos/{idReembolso}/procesar`
