# Product API

## Requisitos previos
- Java 17 o superior
- Maven
- MySQL

## Pasos para levantar el proyecto

1. **Clona el repositorio:**
   ```bash
   git clone <URL-del-repositorio>
   ```

2. **Configura la base de datos:**
   - Crea una base de datos llamada `examen_tecnico` en MySQL.
   - Actualiza las credenciales en `src/main/resources/application.yml`.

3. **Levanta el proyecto:**
   - Desde la raíz del proyecto ejecuta:
     ```bash
     ./mvnw spring-boot:run
     ```
   - El proyecto creará automáticamente las tablas necesarias gracias a la configuración `spring.jpa.hibernate.ddl-auto: update`.

4. **Crea el stored procedure y el trigger:**
   - Una vez que las tablas estén creadas, ejecuta en MySQL:

     ```sql
     -- Alta de Usuario
     DELIMITER $$
     CREATE PROCEDURE USUARIOALT(
         IN p_nombreUsuario VARCHAR(15),
         IN p_contrasenia VARCHAR(100),
         IN p_primerNombre VARCHAR(50),
         IN p_apellidoPaterno VARCHAR(50),
         IN p_apellidoMaterno VARCHAR(50),
         IN p_fechaNacimiento DATE,
         IN p_genero CHAR(1),
         IN p_estadoNacimiento VARCHAR(50),
         IN p_fechaRegistro DATETIME,
         IN p_fechaActualizacion DATETIME
     )
     proc: BEGIN
         DECLARE v_id INT DEFAULT NULL;
         DECLARE Err_Codigo INT DEFAULT 0;
         DECLARE Err_Mensaj VARCHAR(255) DEFAULT '';

         START TRANSACTION;

         -- Comprobar existencia
         SELECT usuario_id INTO v_id
         FROM usuario
         WHERE nombre_usuario = p_nombreUsuario
         LIMIT 1;

         IF v_id IS NOT NULL THEN
            SET Err_Codigo = 40405;
            SET Err_Mensaj = 'Nombre de usuario ya existe';
            ROLLBACK;
            SELECT Err_Codigo, Err_Mensaj;
            LEAVE proc;
         END IF;

         INSERT INTO usuario (
        nombre_usuario, contrasenia, primer_nombre, apellido_paterno, apellido_materno,
        fecha_nacimiento, genero, estado_nacimiento, fecha_registro, fecha_actualizacion
         ) VALUES (
            p_nombreUsuario, p_contrasenia, p_primerNombre, p_apellidoPaterno, p_apellidoMaterno,
            p_fechaNacimiento, p_genero, p_estadoNacimiento, p_fechaRegistro, p_fechaActualizacion
         );
         COMMIT;

         SELECT usuario_id, nombre_usuario, contrasenia, primer_nombre, apellido_paterno,
               apellido_materno, fecha_nacimiento, genero, estado_nacimiento, fecha_registro,
               fecha_actualizacion
         FROM usuario
         WHERE usuario_id = LAST_INSERT_ID();
     END$$
     DELIMITER ;

     -- Trigger para auditar cambios de contraseña
     ALTER TABLE usuario_auditoria MODIFY fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
     DELIMITER $$
     CREATE TRIGGER audit_user_password
     AFTER UPDATE ON usuario
     FOR EACH ROW
     BEGIN
         IF OLD.contrasenia <> NEW.contrasenia THEN
             INSERT INTO usuario_auditoria (usario_id, contrasenia_anterior, contrasenia_nueva)
             VALUES (NEW.usuario_id, OLD.contrasenia, NEW.contrasenia);
         END IF;
     END$$
     DELIMITER ;
     ```
### Scripts importantes para auditoría de contraseñas

- Ejecutar el siguiente script para que la tabla `usuario_auditoria` en el campo `fecha_cambio` obtenga el valor de la fecha y hora de BD:

```sql
ALTER TABLE usuario_auditoria MODIFY fecha_cambio TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
```

- Ejecutar el siguiente script para agregar el trigger que guardará un registro cada que se actualice la contraseña desde el endpoint:

```sql
DELIMITER $$
CREATE TRIGGER audit_user_password
AFTER UPDATE ON usuario
FOR EACH ROW
BEGIN
    IF OLD.contrasenia <> NEW.contrasenia THEN
        INSERT INTO usuario_auditoria (usario_id, contrasenia_anterior, contrasenia_nueva)
        VALUES (NEW.usuario_id, OLD.contrasenia, NEW.contrasenia);
    END IF;
END$$
DELIMITER ;
```

5. **Importa las colecciones de Postman:**
   - En la carpeta del proyecto encontrarás el archivo `Examen Tecnico.postman_collection.json`.
   - Importa la colección en Postman para probar los endpoints.

## Endpoints principales

### Usuarios
- `POST /users/new` - Crear un nuevo usuario
- `PUT /users/update` - Actualizar datos de usuario
- `GET /users/{id}` - Consultar usuario por ID
- `GET /users` - Listar todos los usuarios
- `PATCH /users/{id}/password` - Actualizar contraseña de usuario
- `GET /users/search-like?searchText=valor` - Buscar usuario por nombre, apellido paterno o materno (parcial)
- `GET /users/username/{userName}` - Consultar usuario por nombre de usuario

### Productos
- `POST /products/new` - Crear un nuevo producto
- `GET /products` - Listar productos (opcional: ?active=true/false)
- `GET /products/{id}` - Consultar producto por ID
- `PATCH /products/{id}/active` - Actualizar estado activo/inactivo de producto
- `POST /products/buy-multiple` - Comprar múltiples productos
- `PUT /products/{id}` - Actualizar todos los datos de un producto
- `DELETE /products/{id}` - Eliminar producto y su inventario

> Nota: No es recomendable realizar un delete para eliminar completamente un registro. Lo correcto sería inactivarlo, ya que eliminarlo puede provocar pérdida de integridad de datos.

### Inventario de productos
- `GET /inventory` - Listar todos los inventarios
- `GET /inventory/{id}` - Consultar inventario por ID de producto

### Autenticación
- `POST /auth/login` - Login de usuario y generación de token

## Notas
- El proyecto usa Log4j2 para logs, que se guardan en la carpeta `logs/productos`.
- La configuración de la base de datos y otros parámetros se encuentran en `src/main/resources/application.yml`.

## Contacto
Para dudas o soporte, contacta a angelica.contreras.j20@gmail.com.
