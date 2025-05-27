//ClienteServicesImpl.java

package com.microService.demo.services.impl;

import com.microService.demo.dto.ClienteDTO;
import com.microService.demo.model.Cliente;
import com.microService.demo.repository.IClienteRepository;
import com.microService.demo.services.IClienteServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteServicesImpl implements IClienteServices {
    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO findById(Long id) {
        return clienteRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public ClienteDTO findByClienteId(String clienteId) {
        return clienteRepository.findByClienteId(clienteId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public ClienteDTO save(ClienteDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    @Override
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        if (clienteRepository.existsById(id)) {
            Cliente cliente = convertToEntity(clienteDTO);
            cliente.setId(id);
            Cliente updatedCliente = clienteRepository.save(cliente);
            return convertToDTO(updatedCliente);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setGenero(cliente.getGenero());
        dto.setEdad(cliente.getEdad());
        dto.setIdentificacion(cliente.getIdentificacion());
        dto.setDireccion(cliente.getDireccion());
        dto.setTelefono(cliente.getTelefono());
        dto.setClienteId(cliente.getClienteId());
        dto.setContrasena(cliente.getContrasena());
        dto.setEstado(cliente.isEstado());
        return dto;
    }

    private Cliente convertToEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setNombre(dto.getNombre());
        cliente.setGenero(dto.getGenero());
        cliente.setEdad(dto.getEdad());
        cliente.setIdentificacion(dto.getIdentificacion().longValue());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setClienteId(dto.getClienteId());
        cliente.setContrasena(dto.getContrasena());
        cliente.setEstado(dto.isEstado());
        return cliente;
    }
}
