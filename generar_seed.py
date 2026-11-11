"""Genera fixtures compatibles con los endpoints de FullComponents.

Uso: python generar_seed.py /ruta/a/datos.json
Los valores de usuario/sesión/pago NO se leen ni exportan.
"""
import json
import re
import sys
import unicodedata
from pathlib import Path


def slug(valor):
    normal = unicodedata.normalize("NFKD", valor)
    ascii_ = "".join(c for c in normal if not unicodedata.combining(c))
    return re.sub(r"[^a-z0-9]+", "_", ascii_.lower()).strip("_")[:80]


def guardar(ruta, datos):
    ruta.write_text(json.dumps(datos, indent=2, ensure_ascii=False) + "\n", encoding="utf-8")


def generar(origen, destino):
    todo = json.loads(origen.read_text(encoding="utf-8"))
    valor = todo["localStorage"]["fc-catalogo-admin"]
    catalogo = json.loads(valor) if isinstance(valor, str) else valor
    if len(catalogo) != 15:
        raise ValueError(f"Esperábamos 15 productos; encontramos {len(catalogo)}")
    destino.mkdir(parents=True, exist_ok=True)

    nombres = list(dict.fromkeys(p["categoria"] for p in catalogo))
    categorias = [{"nombreCategoria": nombre} for nombre in nombres]
    id_categoria = {nombre: idx + 1 for idx, nombre in enumerate(nombres)}

    definiciones = []
    id_def = {}
    for nombre in nombres:
        claves = list(dict.fromkeys(k for p in catalogo if p["categoria"] == nombre
                                    for k in p.get("especificaciones", {})))
        for etiqueta in claves:
            clave = slug(etiqueta)
            if (nombre, clave) in id_def:
                raise ValueError(f"Clave de atributo duplicada en {nombre}: {etiqueta}")
            id_def[(nombre, clave)] = len(definiciones) + 1
            definiciones.append({
                "categoria": {"idCategoria": id_categoria[nombre]},
                "clave": clave,
                # Getter/setter reales de DefinicionAtributo.java: getNombreAtributo/setNombreAtributo.
                "nombreAtributo": etiqueta,
                "tipoDato": "TEXTO",
                "unidad": None,
                "filtrable": True,
            })

    productos = []
    stocks = []
    origen_productos = []
    for p in catalogo:
        ruta = p["imagen"].replace("\\", "/").lstrip("/")
        ext = ruta.rsplit(".", 1)[-1].lower()
        productos.append({
            "nombreProducto": p["nombre"],
            "marca": p["marca"],
            "modelo": p["modelo"],
            "descripcion": p["descripcion"],
            "precioVigente": p["precio"],
            "estado": "PUBLICADO",
            "categoria": {"idCategoria": id_categoria[p["categoria"]]},
            "imagenes": [{"url": ruta, "formato": ext, "orden": 0, "esPrincipal": True}],
            "especificaciones": [
                {"valor": str(v), "definicionAtributo": {"idDefinicionAtributo": id_def[(p["categoria"], slug(k))]}}
                for k, v in p.get("especificaciones", {}).items()
            ],
        })
        stocks.append({
            "idProducto": p["id"],
            "cantidadFisica": p["stock"],
            "cantidadReservada": 0,
            "stockMinimo": 2,
        })
        origen_productos.append({"idOriginal": p["id"], "categoria": p["categoria"]})

    guardar(destino / "categorias.json", categorias)
    guardar(destino / "definiciones.json", definiciones)
    guardar(destino / "productos.json", productos)
    guardar(destino / "stock.json", stocks)
    guardar(destino / "mapa_origen.json", {"productos": origen_productos})
    print(f"Generadas {len(categorias)} categorías, {len(definiciones)} atributos, "
          f"{len(productos)} productos, {len(stocks)} stocks en {destino}")


if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("Uso: python generar_seed.py datos.json")
    generar(Path(sys.argv[1]), Path(__file__).resolve().parent / "datos")
