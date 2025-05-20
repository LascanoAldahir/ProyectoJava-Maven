# Microservicio Bancario
Show Image
Este proyecto implementa un sistema bancario utilizando microservicios con Spring Boot. Proporciona funcionalidades para la gestión de clientes, cuentas, movimientos y reportes financieros.
## 📋 Características Implementadas

F1: Operaciones CRUD completas para Clientes, Cuentas y Movimientos
F2: Registro de transacciones financieras con actualización automática de saldos
F3: Validación de saldo disponible con mensajes de error apropiados

## 🛠️ Tecnologías Utilizadas

Java 21
Spring Boot 3.4.5
Spring Data JPA
PostgreSQL
Lombok
Maven

## 🚀 Guía de Pruebas
Las siguientes secciones detallan cómo probar cada funcionalidad del sistema utilizando Postman.
## F1: Operaciones CRUD
## 📌 Cliente
### Crear Cliente (POST)

URL: 

```
http://localhost:8080/clientes
```
Método: POST
Headers: Content-Type: application/json
Body:
```
json{
  "nombre": "José Lema",
  "genero": "Masculino",
  "edad": 35,
  "identificacion": 123456789,
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "clienteId": "jose.lema",
  "contrasena": "1234",
  "estado": true
}
```
Show Image

## Obtener Todos los Clientes (GET)

URL: 
```
http://localhost:8080/clientes
```
Método: GET

## Obtener Cliente por ID (GET)

URL: 
```
http://localhost:8080/clientes/{id}
```
Método: GET

## Actualizar Cliente (PUT)

URL: 
```
http://localhost:8080/clientes/{id}
```
Método: PUT
Headers: Content-Type: application/json
Body: (mismo formato que en la creación)

## Eliminar Cliente (DELETE)

URL: 
```
http://localhost:8080/clientes/{id}
```
Método: DELETE

##📌 Cuenta
### Crear Cuenta (POST)

URL: 
```
http://localhost:8080/cuentas
```
Método: POST
Headers: Content-Type: application/json
Body:
```
json{
  "numeroCuenta": 478758,
  "tipoCuenta": "Ahorro",
  "saldoInicial": 2000,
  "estado": true,
  "clienteId": "jose.lema"
}
```
Show Image
### Obtener Todas las Cuentas (GET)

URL: 
```
http://localhost:8080/cuentas
```
Método: GET

### Obtener Cuenta por Número (GET)

URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```
Método: GET
Show Image

### Actualizar Cuenta (PUT)

URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```
Método: PUT
Headers: Content-Type: application/json
Body: (mismo formato que en la creación)

## Eliminar Cuenta (DELETE)

URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```

Método: DELETE

## F2 & F3: Gestión de Movimientos y Validación de Saldo
###📝 Flujo de prueba completo
## 1️⃣ Crear un depósito (valor positivo)

URL: 
```
http://localhost:8080/movimientos
```
Método: POST
Headers: Content-Type: application/json
Body:
```
json{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Depósito",
  "valor": 500,
  "saldo": 0,
  "numeroCuenta": 478758
}

```
### Resultado esperado:

Estado: 201 Created
El saldo de la cuenta se incrementa a 2500
Show Image

## 2️⃣ Crear un retiro (valor negativo)

URL: 
```
http://localhost:8080/movimientos
```
Método: POST
Headers: Content-Type: application/json
Body:
```
json{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Retiro",
  "valor": -300,
  "saldo": 0,
  "numeroCuenta": 478758
}
```
### Resultado esperado:

Estado: 201 Created
El saldo de la cuenta se reduce a 2200
Show Image

## 3️⃣ Verificar saldo actualizado

URL: 
```
http://localhost:8080/cuentas/478758
```
Método: GET

### Resultado esperado:

Estado: 200 OK
Respuesta que incluye "saldoInicial": 2200
Show Image

## 4️⃣ Validar protección de saldo insuficiente (F3)

URL: 
```
http://localhost:8080/movimientos
```
Método: POST
Headers: Content-Type: application/json
Body:
```
json{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Retiro",
  "valor": -3000,
  "saldo": 0,
  "numeroCuenta": 478758
}

```
### Resultado esperado:

Estado: 400 Bad Request
Mensaje de error: "Saldo no disponible"
Show Image

## 5️⃣ Consultar historial de movimientos

URL: 
```
http://localhost:8080/movimientos/cuenta/478758
```
Método: GET

### Resultado esperado:

Estado: 200 OK
Lista de todos los movimientos realizados en la cuenta
Show Image

## 6️⃣ Modificar un movimiento existente

Primero, obtén el ID del movimiento:

URL: 
```
http://localhost:8080/movimientos
```
Método: GET


Luego actualiza:

URL: 
```
http://localhost:8080/movimientos/{id}
```
Método: PUT
Headers: Content-Type: application/json
Body:


```
json{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Depósito",
  "valor": 600,
  "saldo": 0,
  "numeroCuenta": 478758
}
```
### Resultado esperado:

Estado: 200 OK
El saldo de la cuenta se ajusta automáticamente

## 7️⃣ Eliminar un movimiento

URL: 
```
http://localhost:8080/movimientos/{id}
```
Método: DELETE

### Resultado esperado:

Estado: 204 No Content
El saldo de la cuenta se revierte automáticamente
Show Image

## 📊 Estado de Cuenta (F4)
Aunque esta funcionalidad no está completamente implementada en esta fase, podemos preparar la estructura para futuras versiones. Se puede probar el endpoint parcialmente con la siguiente solicitud:

URL: 
```
http://localhost:8080/reportes?clienteId=jose.lema&fechaInicio=2023-05-01&fechaFin=2023-05-30
```
Método: GET

El resultado esperado sería un JSON con la estructura de un estado de cuenta, pero posiblemente con datos limitados en esta fase.
## 🗂️ Estructura del Proyecto
```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── microService/
│   │           └── demo/
│   │               ├── Controller/
│   │               │   ├── ClienteController.java
│   │               │   ├── CuentaController.java
│   │               │   ├── MovimientoController.java
│   │               │   └── ReporteController.java
│   │               ├── dto/
│   │               │   ├── ClienteDTO.java
│   │               │   ├── CuentaDTO.java
│   │               │   ├── EstadoCuentaDTO.java
│   │               │   ├── CuentaSaldoDTO.java
│   │               │   ├── MovimientoDTO.java
│   │               │   └── MovimientoReporteDTO.java
│   │               ├── exception/
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── model/
│   │               │   ├── Cliente.java
│   │               │   ├── Cuenta.java
│   │               │   └── Movimiento.java
│   │               ├── repository/
│   │               │   ├── IClienteRepository.java
│   │               │   ├── ICuentaRepository.java
│   │               │   └── IMovimientoRepository.java
│   │               ├── services/
│   │               │   ├── IClienteServices.java
│   │               │   ├── ICuentaServices.java
│   │               │   ├── IMovimientoServices.java
│   │               │   └── impl/
│   │               │       ├── ClienteServicesImpl.java
│   │               │       ├── CuentaServicesImpl.java
│   │               │       └── MovimientoServiceImpl.java
│   │               └── UsuarioApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/
            └── microService/
                └── demo/
                    ├── controller/
                    │   └── ClienteControllerTest.java
                    └── service/
                        └── ClienteServiceTest.java

```
## 📝 Notas Importantes

Para que las pruebas funcionen correctamente, asegúrate de crear primero un cliente y una cuenta.
Todos los movimientos negativos (retiros) están validados para prevenir sobregiros.
La base de datos se reinicia en cada ejecución (modo create-drop). Para persistencia, cambia esta configuración en application.properties.

## 🚀 Próximos Pasos

Implementación completa de F4: Reporte de estado de cuenta
Pruebas unitarias adicionales
Configuración para Docker
Documentación con Swagger/OpenAPI

📄 Licencia
Este proyecto está licenciado bajo los términos de la licencia MIT.

⭐️ Desarrollado como parte de la prueba técnica de Arquitectura Microservicio (2023) ⭐️
