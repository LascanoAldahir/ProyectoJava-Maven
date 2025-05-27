//CuentaServicesImpl.java

package com.microService.demo.services.impl;

import com.microService.demo.dto.CuentaDTO;
import com.microService.demo.model.Cliente;
import com.microService.demo.model.Cuenta;
import com.microService.demo.repository.IClienteRepository;
import com.microService.demo.repository.ICuentaRepository;
import com.microService.demo.services.ICuentaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServicesImpl implements ICuentaServices {

    @Autowired
    private ICuentaRepository cuentaRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    @Override
    public List<CuentaDTO> findAll() {
        return cuentaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO findByNumeroCuenta(Long numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<CuentaDTO> findByClienteId(Long clienteId) {
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDTO save(CuentaDTO cuentaDTO) {
        Cuenta cuenta = convertToEntity(cuentaDTO);
        Cuenta savedCuenta = cuentaRepository.save(cuenta);
        return convertToDTO(savedCuenta);
    }

    @Override
    public CuentaDTO update(Long numeroCuenta, CuentaDTO cuentaDTO) {
        if (cuentaRepository.existsById(numeroCuenta)) {
            Cuenta cuenta = convertToEntity(cuentaDTO);
            cuenta.setNumeroCuenta(numeroCuenta);
            Cuenta updatedCuenta = cuentaRepository.save(cuenta);
            return convertToDTO(updatedCuenta);
        }
        return null;
    }

    @Override
    public void delete(Long numeroCuenta) {
        cuentaRepository.deleteById(numeroCuenta);
    }

    // MÉTODO CORREGIDO: Convierte String a Long para numeroCuenta
    private CuentaDTO convertToDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setNumeroCuenta(String.valueOf(cuenta.getNumeroCuenta())); // Long -> String
        dto.setTipoCuenta(cuenta.getTipoCuenta());
        dto.setSaldoInicial(String.valueOf(cuenta.getSaldoInicial())); // Double -> String
        dto.setEstado(cuenta.isEstado());

        if (cuenta.getCliente() != null) {
            dto.setClienteId(cuenta.getCliente().getClienteId());
        }
        return dto;
    }

    // MÉTODO CORREGIDO: Convierte String a Long/Double para la entidad
    private Cuenta convertToEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();

        // Convertir String a Long para numeroCuenta
        try {
            cuenta.setNumeroCuenta(Long.parseLong(dto.getNumeroCuenta()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Número de cuenta inválido: " + dto.getNumeroCuenta());
        }

        cuenta.setTipoCuenta(dto.getTipoCuenta());

        // Convertir String a Double para saldoInicial
        try {
            cuenta.setSaldoInicial(Double.parseDouble(dto.getSaldoInicial()));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Saldo inicial inválido: " + dto.getSaldoInicial());
        }

        cuenta.setEstado(dto.isEstado());

        // Buscar cliente por clienteId
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findByClienteId(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            cuenta.setCliente(cliente);
        }

        return cuenta;
    }
}