# Microservicio Bancario
Show Image
Este proyecto implementa un sistema bancario utilizando microservicios con Spring Boot. Proporciona funcionalidades para la gestión de clientes, cuentas, movimientos y reportes financieros.
## 📋 Características Implementadas

F1: Operaciones CRUD completas para Clientes, Cuentas y Movimientos <br>
F2: Registro de transacciones financieras con actualización automática de saldos <br>
F3: Validación de saldo disponible con mensajes de error apropiados <br>

## 🛠️ Tecnologías Utilizadas

Java 21 <br>
Spring Boot 3.4.5 <br>
Spring Data JPA <br>
PostgreSQL <br>
Lombok <br>
Maven <br>

## 🚀 Guía de Pruebas
Las siguientes secciones detallan cómo probar cada funcionalidad del sistema utilizando Postman.
## F1: Operaciones CRUD

## 📌 Cliente
### Crear Cliente (POST)
Método: POST <br>
Headers: Content-Type: application/json <br>
URL:
```
http://localhost:8082/clientes
```
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
### Creación Exitosa

![image](https://github.com/user-attachments/assets/fe43e6c7-730a-4d03-a1d9-ec0d85c7a2c9)

### Crear un usuario existente

![image](https://github.com/user-attachments/assets/42efc4a6-94f9-4a83-bff5-a970f59b2d4d)

### Id o datos no validos

![image](https://github.com/user-attachments/assets/c556de13-f015-4c45-915c-79787452d35f)

## Obtener Todos los Clientes (GET)

Método: GET
URL: 

```
http://localhost:8082/clientes
```

![image](https://github.com/user-attachments/assets/41558e4f-8e4d-4a4e-a50d-06266559aafc)


## Obtener Cliente por ID (GET)
Método: GET
URL: 
```
http://localhost:8082/clientes/{id}
```
![image](https://github.com/user-attachments/assets/2cbdd020-e551-4bbf-87da-53fa2ab8618e)

## Obtener Cliente por ID (NO EXISTENTE)
![image](https://github.com/user-attachments/assets/3a67f161-b1d7-4e2a-90c0-9e2ced60b2b3)


## Actualizar Cliente (PUT)
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8082/clientes/{id}
```
![image](https://github.com/user-attachments/assets/16f98874-361d-4eba-888c-2f72aebf0ff0)


Body: (mismo formato que en la creación)

## Eliminar Cliente (DELETE)
Método: DELETE <br>
URL: 
```
http://localhost:8082/clientes/{id}
```
Ejecutamos el metodo Delete.
![image](https://github.com/user-attachments/assets/66afe1fe-1533-4372-a918-cd0cc76424f2)

Para despues verificar que este se haya eliminado enlistando a los clientes, deberia aparecer nada mas uno, porque el segundo ya fue eliminado.
![image](https://github.com/user-attachments/assets/502974da-a57f-494b-8eb6-68de7a815a15)



## 📌 Cuenta

### Crear Cuenta (POST)

Método: POST <br>
Headers: Content-Type <br> application/json <br>

URL: 

```
http://localhost:8082/cuentas
```

Body:

```
{
  "numeroCuenta": 478758,
  "tipoCuenta": "Ahorro",
  "saldoInicial": 2000,
  "estado": true,
  "clienteId": "jose.lema"
}
```

![image](https://github.com/user-attachments/assets/ae8736a3-4585-4bb4-9404-e09ebcc65bf0)


## Crear Cuenta a Usuario NO EXISTENTE

![image](https://github.com/user-attachments/assets/9932e334-d5c3-42c1-b510-f436a6927f6a)



### Obtener Todas las Cuentas (GET)
Método: GET <br>
URL: 
```
http://localhost:8082/cuentas
```

![image](https://github.com/user-attachments/assets/d9b3cf95-5427-4c30-b63c-77dd74926ef9)


### Obtener Cuenta por Número (GET)
Método: GET <br>
URL: 
```
http://localhost:8082/cuentas/{numeroCuenta}
```
![image](https://github.com/user-attachments/assets/0ef7922a-e0f7-42ea-9a79-6479fcd49752)


### Actualizar Cuenta (PUT)
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8082/cuentas/{numeroCuenta}
```

Body: (mismo formato que en la creación)

![image](https://github.com/user-attachments/assets/ad8448e8-641e-4e28-90ed-b7a60113ed37)


## Eliminar Cuenta (DELETE)
Método: DELETE <br>
URL: 
```
http://localhost:8082/cuentas/{numeroCuenta}
```

![image](https://github.com/user-attachments/assets/a115bf78-a642-4c04-9a2d-577a1e804b94)



## F2 & F3: Gestión de Movimientos y Validación de Saldo

## 1️⃣ Crear un depósito (valor positivo)

Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 

```
http://localhost:8082/movimientos
```

Body:
```
{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Depósito",
  "valor": 500,
  "saldo": 0,
  "numeroCuenta": 478758
}

```

![image](https://github.com/user-attachments/assets/0d68add1-4a74-4bfd-ad22-8f718695e13a)


## 2️⃣ Crear un retiro (valor negativo)

Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 

```
http://localhost:8082/movimientos

```

Body:

```
{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Retiro",
  "valor": -300,
  "saldo": 0,
  "numeroCuenta": 478758
}
```

![image](https://github.com/user-attachments/assets/a20ed80f-4a67-4f4d-98cd-ae0b4461cf5a)

## 3️⃣ Verificar saldo actualizado

Método: GET <br>

URL: 

```
http://localhost:8082/cuentas/478758
```

![image](https://github.com/user-attachments/assets/35629b23-94e9-4d57-b44e-1898e9b1b4d2)


## 4️⃣ Validar protección de saldo insuficiente (F3)

Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 

```
http://localhost:8082/movimientos
```

Body:

```
{
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

![image](https://github.com/user-attachments/assets/47b4f8dd-0685-450a-bf0d-cc1f78853efd)



## 5️⃣ Consultar historial de movimientos
Método: GET
URL: 

```
http://localhost:8082/movimientos/cuenta/478758
```

### Resultado esperado:

Estado: 200 OK <br>
Lista de todos los movimientos realizados en la cuenta <br>

![image](https://github.com/user-attachments/assets/fc92ac8b-388b-4e00-8f08-0f68d36150b4)
<br>
![image](https://github.com/user-attachments/assets/a0c73d2a-b636-47b5-a87e-16b331ff4716)



## 6️⃣ Modificar un movimiento existente
Primero, obtén el ID del movimiento:
<br>
Método: GET
URL: 

```
http://localhost:8082/movimientos
```

Luego actualiza: <br>
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 

```
http://localhost:8082/movimientos/{id}
```

Body:

```
{
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

![image](https://github.com/user-attachments/assets/26465ca0-d29c-4da4-aadc-60d1c10c03f3)

## 7️⃣ Eliminar un movimiento

Método: DELETE
URL: 
```
http://localhost:8082/movimientos/{id}
```


### Resultado esperado:

Estado: 204 No Content <br>
El saldo de la cuenta se revierte automáticamente <br>

![image](https://github.com/user-attachments/assets/54d2ca51-b619-48b2-ab6d-73203534dda8)



## 📊 Estado de Cuenta (F4)
Aunque esta funcionalidad no está completamente implementada en esta fase, podemos preparar la estructura para futuras versiones. Se puede probar el endpoint parcialmente con la siguiente solicitud:
<br>
Método: GET
<br>
URL: 
```
http://localhost:8080/reportes
```


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
<br>
La base de datos se reinicia en cada ejecución (modo create-drop). Para persistencia, cambia esta configuración en application.properties.

## 🚀 Próximos Pasos

Implementación completa de F4: Reporte de estado de cuenta <br>
Pruebas unitarias adicionales <br>
Configuración para Docker <br>
Documentación con Swagger/OpenAPI <br>

📄 Licencia <br>
Este proyecto está licenciado bajo los términos de la licencia MIT.
<br>
⭐️ Desarrollado como parte de la prueba técnica de Arquitectura Microservicio (2023) ⭐️
