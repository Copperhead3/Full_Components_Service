# ServiceCarrito

Microservicio de carrito para FullComponents Store.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL/XAMPP
- Lombok
- RestTemplate
- Swagger / OpenAPI

## Puerto
`8082`

## Base de datos
`fullcomponents_carrito`

## Dependencias entre microservicios
Este servicio utiliza RestTemplate para comunicarse con:
- Producto: `http://localhost:8083`
- Inventario: `http://localhost:8084`

## Swagger
`http://localhost:8082/swagger-ui.html`

## Flujo principal
Carrito consulta Producto para precio/estado y consulta Inventario para validar disponibilidad. No reserva stock: la reserva definitiva corresponde al flujo de Pedido.

## Endpoints principales
- POST `/api/v1/carritos/cliente/{idCliente}`
- GET `/api/v1/carritos/cliente/{idCliente}`
- POST `/api/v1/carritos/cliente/{idCliente}/items`
- PUT `/api/v1/carritos/cliente/{idCliente}/items/{idProducto}`
- DELETE `/api/v1/carritos/cliente/{idCliente}/items/{idProducto}`
- DELETE `/api/v1/carritos/cliente/{idCliente}/items`
- PUT `/api/v1/carritos/cliente/{idCliente}/validar-precios`
- GET `/api/v1/carritos/cliente/{idCliente}/validar-checkout`
