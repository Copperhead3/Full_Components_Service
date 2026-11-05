package com.producto.ServiceProducto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.producto.ServiceProducto.model.DefinicionAtributo;

@Repository
public interface DefinicionAtributoRepository extends JpaRepository<DefinicionAtributo, Long> {

    List<DefinicionAtributo> findByCategoria_IdCategoria(Long idCategoria);

    Optional<DefinicionAtributo> findByCategoria_IdCategoriaAndClaveIgnoreCase(Long idCategoria, String clave);
}
