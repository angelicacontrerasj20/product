package com.codecraft.product.controller;

import com.codecraft.product.domain.entity.User;
import com.codecraft.product.service.UserService;
import com.codecraft.product.web.model.UserGetModel;
import com.codecraft.product.web.model.UserModel;
import com.codecraft.product.web.model.UserUpdateModel;
import com.codecraft.product.web.model.UserUpdatePasswordModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de usuarios.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@RestController
@RequestMapping("/users")
@Tag(name = "Usuario", description = "Operaciones relacionadas con usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Endpoint para agregar un nuevo usuario.
     * @param userModel Modelo del usuario a agregar.
     * @return Modelo del usuario agregado.
     */
    @PostMapping("/new")
    @Operation(summary = "Crear un nuevo usuario", description = "Registro de usuario.")
    public UserModel addUser(@RequestBody UserModel userModel) {
        return userService.addUser(userModel);
    }

    /**
     * Endpoint para actualizar los datos de un usuario.
     * @param userUpdateModel Modelo con los datos a actualizar.
     * @return Modelo actualizado del usuario.
     */
    @PutMapping("/update")
    @Operation(summary = "Actualizacion de usuario", description = "Actualización de un usuario existente.")
    public UserModel updateUser(@RequestBody UserUpdateModel userUpdateModel) {
        return userService.updateUser(userUpdateModel);
    }

    /**
     * Endpoint para buscar un usuario por su ID.
     * @param id ID del usuario.
     * @return Optional con el modelo del usuario si existe.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consulta de usuario individual", description = "Consulta de usuario por ID.")
    public Optional<UserModel> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    /**
     * Endpoint para listar todos los usuarios existentes.
     * @return Lista de modelos de usuario.
     */
    @GetMapping
    @Operation(summary = "Consultar lista de usuarios", description = "Consulta de todos los usuarios.")
    public List<UserModel> listUsers() {
        return userService.listUsers();
    }

    /**
     * Endpoint para actualizar la contraseña de un usuario.
     * @param id ID del usuario.
     * @param userUpdatePasswordModel Nueva contraseña en texto plano.
     * @return Modelo actualizado del usuario.
     */
    @PatchMapping("/{id}/password")
    @Operation(summary = "Actualizar contraseña de usuario", description = "Actualiza la contraseña de un usuario por su ID.")
    public UserModel updatePassword(@PathVariable Long id, @RequestBody UserUpdatePasswordModel userUpdatePasswordModel) {
        return userService.updatePassword(id, userUpdatePasswordModel);
    }

    /**
     * Endpoint para consultar usuario por cualquier coincidencia parcial en nombre, apellido paterno o materno.
     * @param searchText Texto de búsqueda parcial.
     * @return Lista de usuarios que coinciden parcialmente en cualquier campo.
     */
    @GetMapping("/search-like")
    @Operation(summary = "Buscar usuario por nombre, apellido paterno o materno (parcial)", description = "Consulta de usuario por cualquier coincidencia parcial en nombre, apellido paterno o materno.")
    public List<UserModel> findByNameAndSurnameLike(@RequestParam String searchText) {
        return userService.findByNameAndSurnameLike(searchText);
    }

    /**
     * Endpoint para buscar un usuario por su nombre de usuario.
     * @param userName Nombre de usuario.
     * @return Modelo del usuario si existe.
     */
    @GetMapping("/username/{userName}")
    @Operation(summary = "Consulta de usuario por nombre de usuario", description = "Consulta de usuario por su nombre de usuario.")
    public UserGetModel findByUserName(@PathVariable String userName) {
        return userService.findByUserName(userName);
    }
}
