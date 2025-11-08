package com.codecraft.product.controller;

import com.codecraft.product.service.UserService;
import com.codecraft.product.web.model.LoginRequestModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

/**
 * Controlador para autenticación de usuarios.
 * Expone endpoints para login y generación de tokens.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Realizar autenticación básica y generar token")
public class AuthController {
    @Autowired
    private UserService userService;

    /**
     * Endpoint para autenticar al usuario y generar un token JWT si las credenciales son válidas.
     * @param request Modelo con usuario y contraseña.
     * @return Token JWT si la autenticación es exitosa.
     */
    @PostMapping("/login")
    @Operation(summary = "Login de usuarios", description = "Validación de usuario y contraseña.")
    public String login(@RequestBody LoginRequestModel request) {
        return userService.authenticateUser(request);
    }
}
