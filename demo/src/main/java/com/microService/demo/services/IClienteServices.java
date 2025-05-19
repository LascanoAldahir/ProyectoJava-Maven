package com.microService.demo.services;

import com.microService.demo.dto.ClienteDTO;

import java.util.List;

public interface IClienteServices {
    // Obtener todos los clientes
    List<ClienteDTO> findAll();

    // Buscar un cliente por su ID
    ClienteDTO findById(Long id);

    // Buscar un cliente por su clienteId (identificador de negocio)
    ClienteDTO findByClienteId(String clienteId);

    // Guardar un nuevo cliente
    ClienteDTO save(ClienteDTO clienteDTO);

    // Actualizar un cliente existente
    ClienteDTO update(Long id, ClienteDTO clienteDTO);

    // Eliminar un cliente
    void delete(Long id);
}
