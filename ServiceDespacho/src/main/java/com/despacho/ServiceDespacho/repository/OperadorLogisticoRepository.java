package com.despacho.ServiceDespacho.repository;

import com.despacho.ServiceDespacho.model.OperadorLogistico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperadorLogisticoRepository extends JpaRepository<OperadorLogistico, Long> {

    List<OperadorLogistico> findByEmpresaIgnoreCase(String empresa);
}
