#Requires -Version 5.1
param([string]$BaseUrl = 'http://localhost:8090')

$ErrorActionPreference = 'Stop'
$ruta = Split-Path -Parent $MyInvocation.MyCommand.Path
$archivo = Join-Path $ruta 'clientes.json'
if (-not (Test-Path -LiteralPath $archivo)) {
    throw "No se encuentra clientes.json en $ruta. Deja ambos archivos en la misma carpeta."
}
$BaseUrl = $BaseUrl.TrimEnd('/')

# ConvertFrom-Json en Windows PowerShell puede devolver el arreglo completo
# como un solo elemento. Esta forma enumera cada cliente por separado.
$clientes = @()
$datos = Get-Content -LiteralPath $archivo -Raw -Encoding UTF8 | ConvertFrom-Json
foreach ($item in $datos) {
    $clientes += $item
}

Write-Host "[1/2] Consultando clientes existentes..." -ForegroundColor Cyan
try {
    $respuesta = Invoke-RestMethod -Uri "$BaseUrl/usuarios/api/clientes" -Method Get -ErrorAction Stop
    $existentes = @()
    foreach ($item in $respuesta) { $existentes += $item }
} catch {
    throw "No fue posible consultar ServiceUsuarios. Verifica Usuarios :8081 y Gateway :8090. $($_.Exception.Message)"
}

Write-Host "[2/2] Registrando clientes que faltan..." -ForegroundColor Cyan
foreach ($c in $clientes) {
    $correo = ([string]$c.correo).Trim()
    if ([string]::IsNullOrWhiteSpace($correo)) {
        throw "Un elemento de clientes.json no tiene correo válido. Revisa el archivo."
    }

    $existe = @($existentes | Where-Object {
        $_ -and ([string]$_.correo).Trim().Equals($correo, [System.StringComparison]::OrdinalIgnoreCase)
    }).Count -gt 0

    if ($existe) {
        Write-Host "  YA EXISTE: $correo" -ForegroundColor Yellow
        continue
    }

    $body = $c | ConvertTo-Json -Depth 8 -Compress
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($body)
    try {
        $creado = Invoke-RestMethod -Uri "$BaseUrl/usuarios/api/clientes" -Method Post -ContentType 'application/json; charset=utf-8' -Body $bytes -ErrorAction Stop
        Write-Host "  CREADO: $correo (ID $($creado.idUsuario))" -ForegroundColor Green
        $existentes += $creado
    } catch {
        Write-Host "  ERROR: $correo : $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                if ($stream) {
                    $reader = New-Object System.IO.StreamReader($stream)
                    $detalle = $reader.ReadToEnd()
                    if ($detalle) { Write-Host "  DETALLE DEL BACKEND: $detalle" -ForegroundColor Yellow }
                }
            } catch { }
        }
        throw
    }
}
Write-Host "Carga de clientes terminada. El ADMIN se crea por SQL con admin_local.sql." -ForegroundColor Green