# GatewaySpring

Gateway de entrada para FullComponents Store.

## Tecnologías
- Java 21
- Spring Boot 4.1.1
- Spring Cloud 2025.1.3
- Spring Cloud Gateway Server WebFlux
- Springdoc OpenAPI / Swagger UI 3.1.1

## Puerto
`8090`

## Swagger centralizado
`http://localhost:8090/swagger-ui.html`

Desde Swagger UI se pueden seleccionar las especificaciones OpenAPI de Producto,
Carrito, Inventario, Despacho, Pedidos, Pago y Usuarios.

## Rutas y puertos verificados en el ZIP recibido

| Microservicio | Puerto | Prefijo |
|---|---:|---|
| Producto | 8083 | `/api/v1/productos/**`, `/api/v1/categorias/**` |
| Carrito | 8082 | `/api/v1/carritos/**` |
| Inventario | 8084 | `/api/v1/inventario/**` |
| Despacho | 8085 | `/api/v1/despachos/**` |
| Pedidos | 8086 | `/api/v1/pedidos/**` |
| Pago | 8087 | `/api/v1/pagos/**` |

## Usuarios: pendiente de confirmar

El ZIP `SERVICIOS FSII.zip` no incluye el proyecto ServiceUsuarios.
Por eso, se dejó una ruta provisional en el puerto `8081`:

- URL pública: `/usuarios/**`
- URL destino: `http://localhost:8081`
- Se aplica `StripPrefix=1`.

Ejemplo: `http://localhost:8090/usuarios/api/v1/clientes` se reenvía a
`http://localhost:8081/api/v1/clientes`.

Cuando se disponga del proyecto Usuarios, conviene verificar su puerto,
sus rutas reales y que publique `/v3/api-docs`.

## Comunicación interna

El Gateway centraliza el acceso externo. Las llamadas internas entre
microservicios mediante `RestTemplate` pueden seguir usando las URLs directas
y puertos internos configurados en cada servicio.

## Ejecución en Windows

Desde la carpeta del proyecto:

```powershell
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

## Comprobación

- Swagger: `http://localhost:8090/swagger-ui.html`
- Salud: `http://localhost:8090/actuator/health`
- Producto: `http://localhost:8090/api/v1/productos`
- Carrito: `http://localhost:8090/api/v1/carritos`
- Inventario: `http://localhost:8090/api/v1/inventario/stock`
- Despacho: `http://localhost:8090/api/v1/despachos`
- Pedidos: `http://localhost:8090/api/v1/pedidos`
- Pago: `http://localhost:8090/api/v1/pagos`
