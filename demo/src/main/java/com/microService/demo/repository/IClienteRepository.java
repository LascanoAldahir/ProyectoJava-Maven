//IClienteRepository

package com.microService.demo.repository;

import com.microService.demo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IClienteRepository  extends JpaRepository<Cliente, Long> {
    // Metodo personalizado para buscar un cliente por su clienteId
    Optional<Cliente> findByClienteId(String clienteId);
}
