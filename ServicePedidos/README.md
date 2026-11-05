# ServicePedidos

Microservicio orquestador de pedidos para FullComponents Store.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Data JPA
- MySQL / XAMPP
- Lombok
- RestTemplate
- Swagger / OpenAPI

## Puerto
`8086`

## Base de datos
`fullcomponents_pedidos`

## Swagger
`http://localhost:8086/swagger-ui.html`

## Comunicación entre servicios

ServicePedidos consume mediante RestTemplate:

- ServiceCarrito: `http://localhost:8082`
- ServiceInventario: `http://localhost:8084`
- ServiceDespacho: `http://localhost:8085`
- ServicePago: `http://localhost:8087`

## Flujo de creación

1. Valida carrito para checkout.
2. Revalida precios.
3. Crea Pedido en estado PENDIENTE.
4. Reserva stock en Inventario.
5. Crea Despacho usando `idPedido`.
6. Calcula total incluyendo costo de despacho.
7. Crea e inicia Pago usando `idPedido`.
8. Confirma reservas si el pago es aprobado.
9. Cambia Pedido a CONFIRMADO.
10. Vacía el carrito.

Si ocurre un error antes de confirmar el pedido, intenta anular pago,
liberar reservas y marca el pedido como CANCELADO.

## Gateway

Posteriormente el servicio será publicado mediante GatewaySpring en el puerto `8090`.
