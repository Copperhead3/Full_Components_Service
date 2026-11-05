package com.producto.ServiceProducto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.producto.ServiceProducto.model.Categoria;
import com.producto.ServiceProducto.model.DefinicionAtributo;
import com.producto.ServiceProducto.repository.CategoriaRepository;
import com.producto.ServiceProducto.repository.DefinicionAtributoRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DefinicionAtributoRepository definicionAtributoRepository;

    @Transactional
    public Categoria crearCategoria(Categoria categoria) {
        if (categoriaRepository.findByNombreCategoriaIgnoreCase(categoria.getNombreCategoria()).isPresent()) {
            throw new RuntimeException("La categoría ya existe.");
        }

        if (categoria.getDefinicionesAtributos() != null) {
            categoria.getDefinicionesAtributos().forEach(definicion -> definicion.setCategoria(categoria));
        }

        return categoriaRepository.save(categoria);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    @Transactional
    public Categoria modificarCategoria(Long idCategoria, Categoria categoria) {
        Categoria categoriaActual = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        categoriaRepository.findByNombreCategoriaIgnoreCase(categoria.getNombreCategoria())
                .filter(encontrada -> !encontrada.getIdCategoria().equals(idCategoria))
                .ifPresent(encontrada -> {
                    throw new RuntimeException("Ya existe otra categoría con ese nombre");
                });

        categoriaActual.setNombreCategoria(categoria.getNombreCategoria());
        return categoriaRepository.save(categoriaActual);
    }

    @Transactional
    public boolean eliminarCategoria(Long idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria).orElse(null);
        if (categoria != null) {
            categoriaRepository.delete(categoria);
            return true;
        }
        return false;
    }

    @Transactional
    public DefinicionAtributo crearDefinicion(Long idCategoria, DefinicionAtributo definicion) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        if (definicionAtributoRepository
                .findByCategoria_IdCategoriaAndClaveIgnoreCase(idCategoria, definicion.getClave())
                .isPresent()) {
            throw new RuntimeException("La categoría ya posee una definición con esa clave");
        }

        definicion.setCategoria(categoria);
        return definicionAtributoRepository.save(definicion);
    }

    public List<DefinicionAtributo> listarDefiniciones(Long idCategoria) {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        return definicionAtributoRepository.findByCategoria_IdCategoria(idCategoria);
    }

    @Transactional
    public DefinicionAtributo modificarDefinicion(Long idDefinicion, DefinicionAtributo datos) {
        DefinicionAtributo definicion = definicionAtributoRepository.findById(idDefinicion)
                .orElseThrow(() -> new RuntimeException("Definición de atributo no encontrada"));

        definicion.setClave(datos.getClave());
        definicion.setNombreDefinicionAtributo(datos.getNombreDefinicionAtributo());
        definicion.setTipoDato(datos.getTipoDato());
        definicion.setUnidad(datos.getUnidad());
        definicion.setFiltrable(datos.getFiltrable());

        return definicionAtributoRepository.save(definicion);
    }

    @Transactional
    public boolean eliminarDefinicion(Long idDefinicion) {
        DefinicionAtributo definicion = definicionAtributoRepository.findById(idDefinicion).orElse(null);
        if (definicion != null) {
            definicionAtributoRepository.delete(definicion);
            return true;
        }
        return false;
    }
}
