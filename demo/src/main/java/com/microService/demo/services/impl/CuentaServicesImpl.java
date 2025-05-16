package com.microService.demo.services.impl;

import com.microService.demo.dto.CuentaDTO;
import com.microService.demo.model.Cliente;
import com.microService.demo.model.Cuenta;
import com.microService.demo.repository.ICuentaRepository;
import com.microService.demo.services.ICuentaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServicesImpl implements ICuentaServices {

    // Inyectamos el repositorio de cuentas
    @Autowired
    private ICuentaRepository cuentaRepository;

    // Dependiendo de tu implementación, podrías necesitar también el repositorio de clientes
    // @Autowired
    // private IClienteRepository clienteRepository;

    @Override
    public List<CuentaDTO> findAll() {
        // Obtenemos todas las cuentas y las convertimos a DTOs
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO findByNumeroCuenta(Long numeroCuenta) {
        // Buscamos una cuenta por su número
        return cuentaRepository.findById(numeroCuenta)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<CuentaDTO> findByClienteId(Long clienteId) {
        // Buscamos todas las cuentas de un cliente específico
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO save(CuentaDTO cuentaDTO) {
        // Convertimos el DTO a entidad
        Cuenta cuenta = convertToEntity(cuentaDTO);
        // Guardamos la entidad
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        // Convertimos la entidad guardada a DTO
        return convertToDTO(savedCuenta);
    }

    @Override
    public CuentaDTO update(Long numeroCuenta, CuentaDTO cuentaDTO) {
        // Verificamos si la cuenta existe
        if (cuentaRepository.existsById(numeroCuenta)) {
            // Convertimos el DTO a entidad
            Cuenta cuenta = convertToEntity(cuentaDTO);
            // Aseguramos que el número de cuenta sea el correcto
            cuenta.setNumeroCuenta(numeroCuenta);
            // Guardamos los cambios
            Cuenta updatedCuenta = cuentaRepository.save(cuenta);
            // Convertimos la entidad actualizada a DTO
            return convertToDTO(updatedCuenta);
        }
        return null;
    }

    @Override
    public void delete(Long numeroCuenta) {
        // Eliminamos la cuenta con el número proporcionado
        cuentaRepository.deleteById(numeroCuenta);
    }

    // Método auxiliar para convertir una entidad Cuenta a CuentaDTO
    private CuentaDTO convertToDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(cuenta.getSaldoInicial());
        dto.setEstado(cuenta.isEstado());

        // Si la cuenta tiene un cliente asociado, obtenemos su clienteId
        if (cuenta.getCliente() != null) {
            dto.setClienteId(cuenta.getCliente().getClienteId());
        }
        return dto;
    }

    // Método auxiliar para convertir un CuentaDTO a entidad Cuenta
    private Cuenta convertToEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setTipoCuenta(dto.getTipoCuenta());
        cuenta.setSaldoInicial(dto.getSaldoInicial());
        cuenta.setEstado(dto.isEstado());

        // Aquí necesitaríamos buscar el cliente por su clienteId
        // Esta parte dependerá de cómo tengas implementado tu repositorio de clientes
        // Por ahora lo dejamos como null
        cuenta.setCliente(null);

        return cuenta;
    }
}