# Compatible con PowerShell 5.1+ y GatewaySpring :8090.
# No elimina ni actualiza datos existentes: crea únicamente lo que falta.
param([string]$BaseUrl = 'http://localhost:8090')

$ErrorActionPreference = 'Stop'
$BaseUrl = $BaseUrl.TrimEnd('/')
$rutaDatos = Join-Path $PSScriptRoot 'datos'

function LeerJson([string]$nombre) {
    $archivo = Join-Path $rutaDatos $nombre
    return @(Get-Content -LiteralPath $archivo -Raw -Encoding UTF8 | ConvertFrom-Json)
}

function ApiGet([string]$ruta) {
    return Invoke-RestMethod -Uri "$BaseUrl$ruta" -Method Get -ErrorAction Stop
}

# Convierte la respuesta de PowerShell en una lista plana de objetos.
# Evita que un System.Object[] se interprete como una categoria o producto.
function ExpandirLista($valor) {
    if ($null -eq $valor) { return }
    if ($valor -is [System.Array]) {
        foreach ($elemento in $valor) { ExpandirLista $elemento }
    } elseif ($valor -is [pscustomobject] -and
              $valor.PSObject.Properties['value'] -and
              $valor.PSObject.Properties['Count']) {
        ExpandirLista $valor.value
    } else {
        Write-Output $valor
    }
}

function ApiLista([string]$ruta) {
    $respuesta = ApiGet $ruta
    ExpandirLista $respuesta
}

# Compara nombres con acentos y también respuestas mal decodificadas
# (por ejemplo, "Tarjetas grÃ¡ficas" frente a "Tarjetas gráficas").
function NormalizarNombre([string]$nombre) {
    if ([string]::IsNullOrWhiteSpace($nombre)) { return '' }
    $texto = $nombre.Trim()

    # Reparar UTF-8 interpretado accidentalmente como Windows-1252.
    if ($texto.Contains('Ã') -or $texto.Contains('Â')) {
        try {
            $bytes = [System.Text.Encoding]::GetEncoding(1252).GetBytes($texto)
            $reparado = [System.Text.Encoding]::UTF8.GetString($bytes)
            if (-not $reparado.Contains([char]0xFFFD)) { $texto = $reparado }
        } catch { }
    }

    # Eliminar diferencias de tildes, espacios y mayúsculas.
    $descompuesto = $texto.Normalize([System.Text.NormalizationForm]::FormD)
    $sinTildes = -join @($descompuesto.ToCharArray() | Where-Object {
        [Globalization.CharUnicodeInfo]::GetUnicodeCategory($_) -ne
            [Globalization.UnicodeCategory]::NonSpacingMark
    })
    return (($sinTildes -replace '\s+', ' ').Trim().ToLowerInvariant())
}

function BuscarCategoria($lista, [string]$nombre) {
    $buscada = NormalizarNombre $nombre
    foreach ($elemento in @($lista)) {
        if ($null -eq $elemento) { continue }
        if ($null -eq $elemento.PSObject.Properties['nombreCategoria']) { continue }
        if ((NormalizarNombre ([string]$elemento.nombreCategoria)) -eq $buscada) {
            return $elemento
        }
    }
    return $null
}

function ApiPost([string]$ruta, $objeto) {
    $json = ConvertTo-Json -InputObject $objeto -Depth 30 -Compress
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($json)
    return Invoke-RestMethod -Uri "$BaseUrl$ruta" -Method Post -Body $bytes -ContentType 'application/json; charset=utf-8' -ErrorAction Stop
}

$categorias = LeerJson 'categorias.json'
$definiciones = LeerJson 'definiciones.json'
$productos = LeerJson 'productos.json'
$stocks = LeerJson 'stock.json'
$mapa = (Get-Content -LiteralPath (Join-Path $rutaDatos 'mapa_origen.json') -Raw -Encoding UTF8 | ConvertFrom-Json)

if ($productos.Count -ne $mapa.productos.Count -or $productos.Count -ne $stocks.Count) {
    throw 'Los archivos JSON no tienen igual cantidad de productos, mapeos y stocks.'
}

Write-Host '[1/4] Comprobando y creando categorías...' -ForegroundColor Cyan
$categoriasExistentes = @(ApiLista '/api/v1/categorias')
$idsCategorias = @{}
for ($ci = 0; $ci -lt $categorias.Count; $ci++) {
    $c = $categorias[$ci]
    $encontrada = BuscarCategoria $categoriasExistentes $c.nombreCategoria
    if ($null -eq $encontrada) {
        ApiPost '/api/v1/categorias/crear' $c | Out-Null
        $categoriasExistentes = @(ApiLista '/api/v1/categorias')
        $encontrada = BuscarCategoria $categoriasExistentes $c.nombreCategoria
    }
    if ($null -eq $encontrada) { throw "No se pudo recuperar categoría $($c.nombreCategoria)" }
    $idCategoria = [long]$encontrada.idCategoria
    $idsCategorias[($ci + 1)] = $idCategoria
    Write-Host "  $($c.nombreCategoria) = ID $idCategoria"
}

Write-Host '[2/4] Comprobando y creando definiciones de atributos...' -ForegroundColor Cyan
$idsAtributos = @{}
foreach ($d in $definiciones) {
    $idAnteriorCategoria = [int]$d.categoria.idCategoria
    $idActualCategoria = $idsCategorias[$idAnteriorCategoria]
    $existentes = @(ApiLista "/api/v1/categorias/$idActualCategoria/atributos")
    $encontrada = $existentes | Where-Object { $_.clave -eq $d.clave } | Select-Object -First 1
    if ($null -eq $encontrada) {
        $d.categoria.idCategoria = $idActualCategoria
        ApiPost "/api/v1/categorias/$idActualCategoria/atributos/crear" $d | Out-Null
        $existentes = @(ApiLista "/api/v1/categorias/$idActualCategoria/atributos")
        $encontrada = $existentes | Where-Object { $_.clave -eq $d.clave } | Select-Object -First 1
    }
    if ($null -eq $encontrada) { throw "No se pudo recuperar definición $($d.clave)" }
    $idsAtributos["$idAnteriorCategoria|$($d.clave)"] = [long]$encontrada.idDefinicionAtributo
}

Write-Host '[3/4] Comprobando y creando los 15 productos...' -ForegroundColor Cyan
$existentes = @(ApiLista '/api/v1/productos')
$idsProductos = @{}
for ($i = 0; $i -lt $productos.Count; $i++) {
    $p = $productos[$i]
    $origen = $mapa.productos[$i]
    $idAnteriorCategoria = [int]$p.categoria.idCategoria
    $p.categoria.idCategoria = $idsCategorias[$idAnteriorCategoria]
    for ($j = 0; $j -lt $p.especificaciones.Count; $j++) {
        $defAnterior = [int]$p.especificaciones[$j].definicionAtributo.idDefinicionAtributo
        $clave = $definiciones[$defAnterior - 1].clave
        $p.especificaciones[$j].definicionAtributo.idDefinicionAtributo = $idsAtributos["$idAnteriorCategoria|$clave"]
    }
    $encontrado = $existentes | Where-Object { $_.marca -eq $p.marca -and $_.modelo -eq $p.modelo } | Select-Object -First 1
    if ($null -eq $encontrado) {
        ApiPost '/api/v1/productos/crear' $p | Out-Null
        $existentes = @(ApiLista '/api/v1/productos')
        $encontrado = $existentes | Where-Object { $_.marca -eq $p.marca -and $_.modelo -eq $p.modelo } | Select-Object -First 1
    }
    if ($null -eq $encontrado) { throw "No se pudo recuperar producto $($p.nombreProducto)" }
    $idsProductos[[int]$origen.idOriginal] = [long]$encontrado.idProducto
    Write-Host "  Producto $($origen.idOriginal) => ID $($encontrado.idProducto): $($p.nombreProducto)"
}

Write-Host '[4/4] Comprobando y creando los registros de inventario...' -ForegroundColor Cyan
$existentesStock = @(ApiLista '/api/v1/inventario/stock')
foreach ($s in $stocks) {
    $idAnterior = [int]$s.idProducto
    $s.idProducto = $idsProductos[$idAnterior]
    $actual = $existentesStock | Where-Object { [long]$_.idProducto -eq [long]$s.idProducto } | Select-Object -First 1
    if ($null -eq $actual) {
        ApiPost '/api/v1/inventario/stock/registrar' $s | Out-Null
        $existentesStock = @(ApiLista '/api/v1/inventario/stock')
        Write-Host "  Stock creado, producto $($s.idProducto): $($s.cantidadFisica) unidades"
    } else {
        Write-Host "  Stock ya existe para producto $($s.idProducto): no se modifica"
    }
}

Write-Host 'Carga completada. Consulta /api/v1/productos/catalogo y /api/v1/inventario/stock' -ForegroundColor Green