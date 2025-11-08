package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo para la petición de login de usuario.
 * Contiene usuario y contraseña para autenticación.
 */
@Getter
@Setter
public class LoginRequestModel {
    private String username;
    private String password;
}
