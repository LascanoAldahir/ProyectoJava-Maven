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
http://localhost:8080/clientes
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
![image](https://github.com/user-attachments/assets/51a29a68-9023-4377-a161-c9a3dd94b2a8)
<br>
![image](https://github.com/user-attachments/assets/04381132-4f38-4631-a9da-bf98d0ff7b61)

<br>


## Obtener Todos los Clientes (GET)
Método: GET
URL: 
```
http://localhost:8080/clientes
```
![image](https://github.com/user-attachments/assets/c7964e92-7bd8-4694-b794-08222a93616e)

<br>
## Obtener Cliente por ID (GET)
Método: GET
URL: 
```
http://localhost:8080/clientes/{id}
```
![image](https://github.com/user-attachments/assets/19e85284-b0b0-4336-afc3-d6546445ee43)


## Actualizar Cliente (PUT)
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/clientes/{id}
```
![image](https://github.com/user-attachments/assets/a5f84c4e-9a44-48b3-bd2a-afa7b7c792e4)

Body: (mismo formato que en la creación)

## Eliminar Cliente (DELETE)
Método: DELETE <br>
URL: 
```
http://localhost:8080/clientes/{id}
```
Ejecutamos el metodo Delete.
![image](https://github.com/user-attachments/assets/fcdc49db-7597-4c07-8053-cb8d65491e6c)
Para despues verificar que este se haya eliminado enlistando a los clientes, deberia aparecer nada mas uno, porque el segundo ya fue eliminado.
![image](https://github.com/user-attachments/assets/2c43dd2c-6cd3-4f67-991c-64098773c8e4)



## 📌 Cuenta
### Crear Cuenta (POST)
Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/cuentas
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

![image](https://github.com/user-attachments/assets/1ca2da74-cb93-41ac-aaaf-d8406c45880e)



### Obtener Todas las Cuentas (GET)
Método: GET <br>
URL: 
```
http://localhost:8080/cuentas
```
![image](https://github.com/user-attachments/assets/5ba3691d-bcc7-451e-99e5-38a01d545667)


### Obtener Cuenta por Número (GET)
Método: GET <br>
URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```
![image](https://github.com/user-attachments/assets/1fec5c8e-15fa-48fe-ba48-4203a7b6897c)


### Actualizar Cuenta (PUT)
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```

Body: (mismo formato que en la creación)
![image](https://github.com/user-attachments/assets/3d98b4fc-3848-435d-86cf-7b7d67cb6648)


## Eliminar Cuenta (DELETE)
Método: DELETE <br>
URL: 
```
http://localhost:8080/cuentas/{numeroCuenta}
```
![image](https://github.com/user-attachments/assets/164366c4-df36-46a8-8c06-02d019284cc1)



## F2 & F3: Gestión de Movimientos y Validación de Saldo
###📝 Flujo de prueba completo
<br>

## 1️⃣ Crear un depósito (valor positivo)
Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/movimientos
```

Body:
```
json{
  "fecha": "2023-05-19",
  "tipoMovimiento": "Depósito",
  "valor": 500,
  "saldo": 0,
  "numeroCuenta": 110101
}

```

![image](https://github.com/user-attachments/assets/0a875b48-af2a-4daf-9627-d15c75c25c80)


### Resultado esperado:

Estado: 201 Created
El saldo de la cuenta se incrementa a 2500
Show Image
Método: POST<br>
<br>
## 2️⃣ Crear un retiro (valor negativo)
Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/movimientos
```

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
Método: GET
URL: 
```
http://localhost:8080/cuentas/478758
```


### Resultado esperado:

Estado: 200 OK
Respuesta que incluye "saldoInicial": 2200
Show Image

## 4️⃣ Validar protección de saldo insuficiente (F3)
Método: POST <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/movimientos
```

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
Método: GET
URL: 
```
http://localhost:8080/movimientos/cuenta/478758
```


### Resultado esperado:

Estado: 200 OK <br>
Lista de todos los movimientos realizados en la cuenta <br>
Show Image

## 6️⃣ Modificar un movimiento existente

Primero, obtén el ID del movimiento:
<br>
Método: GET
URL: 
```
http://localhost:8080/movimientos
```



Luego actualiza: <br>
Método: PUT <br>
Headers: Content-Type: application/json <br>
URL: 
```
http://localhost:8080/movimientos/{id}
```

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
Método: DELETE
URL: 
```
http://localhost:8080/movimientos/{id}
```


### Resultado esperado:

Estado: 204 No Content <br>
El saldo de la cuenta se revierte automáticamente <br>
Show Image

## 📊 Estado de Cuenta (F4)
Aunque esta funcionalidad no está completamente implementada en esta fase, podemos preparar la estructura para futuras versiones. Se puede probar el endpoint parcialmente con la siguiente solicitud:
<br>
Método: GET
<br>
URL: 
```
http://localhost:8080/reportes?clienteId=jose.lema&fechaInicio=2023-05-01&fechaFin=2023-05-30
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
