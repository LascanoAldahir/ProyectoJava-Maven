package com.microService.demo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microService.demo.dto.ClienteDTO;
import com.microService.demo.services.IClienteServices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IClienteServices clienteServices;



    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /clientes - Lista vacía")
    void testGetAllClientes_empty() throws Exception {
        when(clienteServices.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.mensaje").value("No se encontraron clientes registrados"));
    }

    @Test
    @DisplayName("GET /clientes - Lista con datos")
    void testGetAllClientes_withData() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNombre("Juan Perez");
        cliente.setGenero("Masculino");
        cliente.setEdad(30);
        cliente.setIdentificacion(12345678L);
        cliente.setDireccion("Calle Falsa 123");
        cliente.setTelefono("1234567890");
        cliente.setClienteId("juan123");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        when(clienteServices.findAll()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    @DisplayName("GET /clientes/{id} - Cliente encontrado")
    void testGetClienteById_found() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNombre("Ana");

        when(clienteServices.findById(1L)).thenReturn(cliente);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    @DisplayName("GET /clientes/{id} - Cliente no encontrado")
    void testGetClienteById_notFound() throws Exception {
        when(clienteServices.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cliente no encontrado"));
    }

    @Test
    @DisplayName("POST /clientes - Crear cliente válido")
    void testCreateCliente_valid() throws Exception {
        ClienteDTO request = new ClienteDTO();
        request.setNombre("Carlos");
        request.setGenero("Masculino");
        request.setEdad(25);
        request.setIdentificacion(12345678L);
        request.setDireccion("Av Siempre Viva 742");
        request.setTelefono("0987654321");
        request.setClienteId("carlos01");
        request.setContrasena("pass");
        request.setEstado(true);

        when(clienteServices.findByClienteId("carlos01")).thenReturn(null);
        when(clienteServices.save(any(ClienteDTO.class))).thenReturn(request);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Cliente creado exitosamente"))
                .andExpect(jsonPath("$.cliente.nombre").value("Carlos"));
    }

    @Test
    @DisplayName("DELETE /clientes/{id} - Cliente eliminado")
    void testDeleteCliente_success() throws Exception {
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(1L);
        cliente.setNombre("Eliminar");

        when(clienteServices.findById(1L)).thenReturn(cliente);
        Mockito.doNothing().when(clienteServices).delete(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente eliminado exitosamente."));
    }

    @Test
    @DisplayName("DELETE /clientes/{id} - Cliente no existe")
    void testDeleteCliente_notFound() throws Exception {
        when(clienteServices.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cliente no encontrado"));
    }
}
