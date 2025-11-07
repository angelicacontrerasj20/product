package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Modelo para recibir datos de login en formato JSON.
 */
@Getter
@Setter
public class LoginRequestModel {
    private String username;
    private String password;
}

