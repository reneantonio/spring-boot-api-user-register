
# API RESTful de Creación de Usuarios

Esta API expone endpoints para registrar usuarios y sus telefonos, valida correos de distintos dominios o personalizados que pertenecen únicamente a un dominio especifico como `@dominio.cl`, valida contraseñas, y genera un token JWT al momento de la creación de usuarios.

## Tabla de Contenidos
1. [Descripción](#descripción)
2. [Características](#características)
3. [Requisitos Previos](#requisitos-previos)
4. [Instalación](#instalación)
5. [Configuración](#configuración)
6. [Ejemplos de Uso](#ejemplos-de-uso)
7. [Base de Datos en Memoria](#base-de-datos-en-memoria)
8. [Solución de Problemas](#solución-de-problemas)
9. [Diagrama de la Solución](#diagrama-de-la-solución)
10. [Pruebas Unitarias e Integradas](#pruebas-unitarias-e-integradas)
11. [Documentación de la API con Swagger (OpenAPI 3)](#documentación-de-la-api-con-swagger-openapi-3)
## Descripción

La API permite crear usuarios, validar correos electrónicos y contraseñas que cumplan con ciertos requisitos de seguridad. Al registrar un usuario, se genera un **token JWT** que se devuelve en la respuesta. Esta API utiliza una base de datos en memoria **H2** para almacenamiento temporal, lo que significa que los datos no persisten una vez que la aplicación se detiene.

## Características

- Validación de correos electrónicos.
- Validación de contraseñas con al menos un número, una letra mayúscula y una letra minúscula, y mínimo 8 caracteres.
- Generación de tokens JWT.
- Mensajes de error personalizados configurables desde `application.properties`.
- Base de datos en memoria **H2** (datos temporales).

## Requisitos Previos

Antes de comenzar, asegúrate de tener las siguientes herramientas instaladas en tu máquina:

- **Java 21+**
- **Maven**
- **Git**

## Instalación

Sigue los siguientes pasos para instalar y ejecutar la aplicación:

1. **Clonar el repositorio:**
    ```bash
    git clone https://github.com/reneantonio/spring-boot-api-user-register.git
    cd spring-boot-api-user-register
    ```

2. **Compilar la aplicación:**
    ```bash
    mvn clean install
    ```

3. **Ejecutar la aplicación:**
    ```bash
    mvn spring-boot:run
    ```

4. **Acceso a la API:**
    La API estará disponible en `http://localhost:8080`.

## Configuración

### Clave JWT

La clave secreta utilizada para firmar los tokens JWT esta configurada en el archivo **`application.properties`**.

Ejemplo de configuración de clave secreta en Base64:

```propert
jwt.secret.key=wKQeJzKlW7AkA8dlwefnf9plmRtT2K8jdoJjNfmdklEjN+Ja3Q==
```

### Expresión Regular para Validación

Puedes ajustar las expresiones regulares para la validación del correo y la contraseña en el archivo **`application.properties`**:

```propert
# Expresión regular para correos que aceptan solo @dominio.cl
validation.email.regex=^[\w.-]+@dominio\.cl$

# Expresión regular para contraseñas seguras
validation.password.regex=(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}
```

### Mensajes Personalizados

Los mensajes de error para las validaciones se pueden personalizar desde el archivo **`application.properties`**:

```propert
error.email.already.exists=El correo ya está registrado.
error.email.invalid.format=El formato del correo no es válido.
error.password.invalid=La contraseña no cumple con los requisitos.
```

## Ejemplos de Uso

### 1. Registro de Usuario

- **POST /api/register**
- **Request Body:**
  ```json
  {
      "name": "Juan Rodriguez",
      "email": "juan@rodriguez.org",
      "password": "Password1",
      "phones": [
          {
              "number": "1234567",
              "citycode": "1",
              "contrycode": "57"
          }
      ]
  }
  ```

- **Respuesta de éxito (201):**
  ```json
    {
        "id": "fb9f46d6-c0ac-46bd-a9fd-de3222e9bc87",
        "name": "Juan Rodriguez",
        "email": "juan@rodriguez.org",
        "phones": [
            {
                "id": "aa3688fb-f8dc-4afe-9fd7-0921cf2a54ae",
                "number": "1234567",
                "citycode": "1",
                "contrycode": "57"
            }
        ],
        "created": "2024-10-16T21:07:34.107596048",
        "modified": "2024-10-16T21:07:34.107623263",
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuQHJvZHJpZ3Vlei5vcmciLCJpYXQiOjE3MjkxMjM2NTQsImV4cCI6MTcyOTE1OTY1NH0.pR2u7ho0BRNXxCDuKprGY6WuFjU7NhLatx0aef5gw1o",
        "active": true,
        "last_login": "2024-10-16T21:07:34.052806943",
        "isactive": true
    }
  ```

- **Respuesta de error (400):**
  ```json
  {
      "mensaje": "El formato del correo no es válido."
  }
  ```

### 2. Prueba del Endpoint con `curl`

Puedes probar el registro de usuario usando el siguiente comando `curl`:

```bash
curl -X POST http://localhost:8080/api/register -H "Content-Type: application/json" -d '{
    "name": "Juan Rodriguez",
    "email": "juan@rodriguez.org",
    "password": "Password1",
    "phones": [
        {
            "number": "1234567",
            "citycode": "1",
            "contrycode": "57"
        }
    ]
}'
```

## Base de Datos en Memoria

Esta API utiliza una base de datos en memoria **H2** para el almacenamiento temporal de datos. Esto significa que **los datos no persisten** una vez que la aplicación se detiene.

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: *(dejar en blanco)*

Puedes acceder a la consola de H2 en `http://localhost:8080/h2-console`.

## Solución de Problemas

- **Problema**: No puedo acceder a la API.
    - **Solución**: Verifica que la aplicación se esté ejecutando correctamente usando `mvn spring-boot:run`.

- **Problema**: No puedo acceder a la consola de H2.
    - **Solución**: Asegúrate de que el perfil `h2-console` esté habilitado en `application.properties` con:
      ```propert
      spring.h2.console.enabled=true
      ```
# Diagrama de la Solución

El siguiente diagrama representa el flujo del registro de usuario en la API:

![Diagrama del Proceso de Registro](diagram-user-registration.svg)

## Pruebas Unitarias e Integradas

Las pruebas del proyecto se dividen en **pruebas unitarias** y **pruebas de integración simulada**. Estas pruebas garantizan que las unidades y los componentes del sistema se comporten correctamente.

### Ejecución de Pruebas

Para ejecutar todas las pruebas unitarias e integradas del proyecto, utiliza el siguiente comando:

```bash
mvn test
```

Este comando ejecutará todas las pruebas definidas en el proyecto, incluyendo las pruebas de servicios y controladores.

### Tipos de Pruebas

1. **Pruebas Unitarias (Unit Tests)**:
   Estas pruebas aseguran que las clases individuales (por ejemplo, servicios) se comporten de manera correcta. Utilice **Mockito** para simular dependencias y aislar la unidad bajo prueba.

2. **Pruebas de Integración Simulada (Simulated Integration Tests)**:
   Las pruebas de integración simulada verifican la interacción entre los controladores y servicios simulando solicitudes HTTP usando **MockMvc** y **Mockito**.

## Documentación de la API con Swagger (OpenAPI 3)

Este proyecto usa **Swagger 3** (OpenAPI 3) para generar documentación interactiva de la API REST.

### Acceso a Swagger UI

Una vez que la aplicación esté corriendo, puedes acceder a **Swagger UI** para ver la documentación de la API e interactuar con los endpoints directamente desde el navegador.

- **URL para Swagger UI**: 
  ``` 
  http://localhost:8080/swagger-ui/index.html
  ```

### Acceso a la Documentación en Formato OpenAPI 3 (JSON)

Si deseas obtener la documentación de la API en formato **JSON**, puedes hacerlo en la siguiente URL:

- **URL para la documentación en JSON**: 
  ```
  http://localhost:8080/v3/api-docs
  ```
