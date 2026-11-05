package com.producto.ServiceProducto.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.producto.ServiceProducto.model.Categoria;
import com.producto.ServiceProducto.model.DefinicionAtributo;
import com.producto.ServiceProducto.model.EspecificacionProducto;
import com.producto.ServiceProducto.model.EstadoPublicacion;
import com.producto.ServiceProducto.model.ImagenProducto;
import com.producto.ServiceProducto.model.Producto;
import com.producto.ServiceProducto.repository.CategoriaRepository;
import com.producto.ServiceProducto.repository.DefinicionAtributoRepository;
import com.producto.ServiceProducto.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DefinicionAtributoRepository definicionAtributoRepository;

    @Transactional
    public Producto crearProducto(Producto producto) {
        if (productoRepository
                .findByMarcaIgnoreCaseAndModeloIgnoreCase(producto.getMarca(), producto.getModelo())
                .isPresent()) {
            throw new RuntimeException("Ya existe un producto con la misma marca y modelo");
        }

        prepararProducto(producto);
        return productoRepository.save(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Long idProducto) {
        return productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> listarCatalogo() {
        return productoRepository.findByEstadoOrderByNombreProductoAsc(EstadoPublicacion.PUBLICADO);
    }

    public List<Producto> listarCatalogoPorCategoria(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        return productoRepository.findByCategoria_IdCategoriaAndEstadoOrderByNombreProductoAsc(
                idCategoria,
                EstadoPublicacion.PUBLICADO);
    }

    public List<Producto> buscarPublicados(String texto) {
        if (texto == null || texto.isBlank()) {
            return listarCatalogo();
        }
        return productoRepository.buscarPublicadosPorTexto(texto.trim(), EstadoPublicacion.PUBLICADO);
    }

    @Transactional
    public Producto modificarProducto(Long idProducto, Producto datos) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoRepository
                .findByMarcaIgnoreCaseAndModeloIgnoreCase(datos.getMarca(), datos.getModelo())
                .filter(encontrado -> !encontrado.getIdProducto().equals(idProducto))
                .ifPresent(encontrado -> {
                    throw new RuntimeException("Ya existe otro producto con la misma marca y modelo");
                });

        Categoria categoria = buscarCategoria(datos);

        producto.setNombreProducto(datos.getNombreProducto());
        producto.setMarca(datos.getMarca());
        producto.setModelo(datos.getModelo());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecioVigente(datos.getPrecioVigente());
        producto.setCategoria(categoria);
        producto.setGarantia(datos.getGarantia());
        producto.setInformacionDespacho(datos.getInformacionDespacho());

        if (datos.getImagenes() != null) {
            producto.getImagenes().clear();
            for (ImagenProducto imagen : datos.getImagenes()) {
                imagen.setIdImagenProducto(null);
                imagen.setProducto(producto);
                producto.getImagenes().add(imagen);
            }
        }

        if (datos.getEspecificaciones() != null) {
            producto.getEspecificaciones().clear();
            for (EspecificacionProducto especificacion : datos.getEspecificaciones()) {
                especificacion.setIdEspecificacionProducto(null);
                especificacion.setProducto(producto);
                especificacion.setDefinicionAtributo(buscarDefinicion(especificacion));
                producto.getEspecificaciones().add(especificacion);
            }
        }

        return productoRepository.save(producto);
    }

    @Transactional
    public Producto publicarProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstado(EstadoPublicacion.PUBLICADO);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto despublicarProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setEstado(EstadoPublicacion.DESPUBLICADO);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizarRelacionados(Long idProducto, List<Long> idsRelacionados) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Set<Producto> relacionados = new LinkedHashSet<>();

        if (idsRelacionados != null) {
            for (Long idRelacionado : idsRelacionados) {
                if (idRelacionado == null || idRelacionado.equals(idProducto)) {
                    continue;
                }

                Producto relacionado = productoRepository.findById(idRelacionado)
                        .orElseThrow(() -> new RuntimeException(
                                "Producto relacionado no encontrado: " + idRelacionado));
                relacionados.add(relacionado);
            }
        }

        producto.setRelacionados(relacionados);
        return productoRepository.save(producto);
    }

    @Transactional
    public boolean eliminarProducto(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto).orElse(null);
        if (producto != null) {
            productoRepository.delete(producto);
            return true;
        }
        return false;
    }

    private void prepararProducto(Producto producto) {
        Categoria categoria = buscarCategoria(producto);
        producto.setCategoria(categoria);

        if (producto.getEstado() == null) {
            producto.setEstado(EstadoPublicacion.BORRADOR);
        }

        if (producto.getImagenes() != null) {
            for (ImagenProducto imagen : producto.getImagenes()) {
                imagen.setProducto(producto);
            }
        }

        if (producto.getEspecificaciones() != null) {
            for (EspecificacionProducto especificacion : producto.getEspecificaciones()) {
                especificacion.setProducto(producto);
                especificacion.setDefinicionAtributo(buscarDefinicion(especificacion));
            }
        }
    }

    private Categoria buscarCategoria(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getIdCategoria() == null) {
            throw new RuntimeException("Debe indicar una categoría válida");
        }

        return categoriaRepository.findById(producto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    private DefinicionAtributo buscarDefinicion(EspecificacionProducto especificacion) {
        if (especificacion.getDefinicionAtributo() == null
                || especificacion.getDefinicionAtributo().getIdDefinicionAtributo() == null) {
            throw new RuntimeException("Debe indicar una definición de atributo válida");
        }

        return definicionAtributoRepository
                .findById(especificacion.getDefinicionAtributo().getIdDefinicionAtributo())
                .orElseThrow(() -> new RuntimeException("Definición de atributo no encontrada"));
    }
}
