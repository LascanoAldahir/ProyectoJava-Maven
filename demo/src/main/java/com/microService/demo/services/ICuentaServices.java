package com.microService.demo.services;

import com.microService.demo.dto.CuentaDTO;
import java.util.List;

public interface ICuentaServices {
    // Obtiene todas las cuentas
    List<CuentaDTO> findAll();

    // Busca una cuenta por su número de cuenta
    CuentaDTO findByNumeroCuenta(Long numeroCuenta);

    // Busca todas las cuentas de un cliente específico
    List<CuentaDTO> findByClienteId(Long clienteId);

    // Guarda una nueva cuenta
    CuentaDTO save(CuentaDTO cuentaDTO);

    // Actualiza una cuenta existente
    CuentaDTO update(Long numeroCuenta, CuentaDTO cuentaDTO);

    // Elimina una cuenta
    void delete(Long numeroCuenta);
}