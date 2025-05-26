//ICuentaRepository
package com.microService.demo.repository;

import com.microService.demo.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICuentaRepository extends JpaRepository<Cuenta, Long> {
    // Encuentra todas las cuentas asociadas a un cliente específico por ID
    List<Cuenta> findByClienteId(Long clienteId);
}