$services = @(
    "Usuarios",
    "ServiceProducto",
    "ServiceInventario_lombok",
    "ServiceCarrito",
    "ServicePago",
    "ServiceDespacho",
    "ServicePedidos",
    "GatewaySpring"
)

foreach ($service in $services) {
    Write-Host "Iniciando $service..." -ForegroundColor Green
    # Detecta si existe mvnw.cmd en la carpeta, si no, intenta usar mvn global
    $cmd = "if (Test-Path .\mvnw.cmd) { .\mvnw.cmd spring-boot:run } else { mvn spring-boot:run }"
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd ./$service; $cmd"
    Start-Sleep -Seconds 3
}