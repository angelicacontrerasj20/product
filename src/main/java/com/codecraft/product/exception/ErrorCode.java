package com.codecraft.product.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCode {
    public static final String USER_NOT_FOUND = "40401";
    public static final String PRODUCT_NOT_FOUND = "40402";
    public static final String INVENTORY_NOT_FOUND = "40403";
    public static final String PRODUCT_DUPLICATE = "40404";
    public static final String USER_DUPLICATE = "40405";
    public static final String PRODUCT_PURCHASE_EMPTY = "40903";
    public static final String PRODUCT_PURCHASE_LIMIT = "40904";
    public static final String PRODUCT_INACTIVE = "40905";
    public static final String PRODUCT_NO_STOCK = "40906";
    public static final String PRODUCT_NO_INVENTORY = "40907";
    public static final String USER_PASSWORD_INVALID = "40908";
    public static final String USER_LOGIN_BLOCKED = "40909";
    public static final String GENERAL_ERROR = "50000";
    // Puedes agregar más códigos según tus necesidades

    private static final Map<String, String> errorMessages = new HashMap<>();
    static {
        errorMessages.put(USER_NOT_FOUND, "Usuario no encontrado");
        errorMessages.put(PRODUCT_NOT_FOUND, "Producto no encontrado");
        errorMessages.put(INVENTORY_NOT_FOUND, "Inventario no encontrado");
        errorMessages.put(PRODUCT_DUPLICATE, "Producto existente");
        errorMessages.put(USER_DUPLICATE, "Usuario existente");
        errorMessages.put(PRODUCT_PURCHASE_EMPTY, "No hay productos para comprar");
        errorMessages.put(PRODUCT_PURCHASE_LIMIT, "Solo puedes comprar hasta 5 productos distintos");
        errorMessages.put(PRODUCT_INACTIVE, "Producto inactivo");
        errorMessages.put(PRODUCT_NO_STOCK, "Sin stock suficiente");
        errorMessages.put(PRODUCT_NO_INVENTORY, "Sin inventario");
        errorMessages.put(USER_PASSWORD_INVALID, "Usuario o contraseña inválidos");
        errorMessages.put(USER_LOGIN_BLOCKED, "Usuario bloqueado por intentos fallidos de login");
        errorMessages.put(GENERAL_ERROR, "Error general del sistema");
        // Agrega aquí más códigos y mensajes
    }

    public static String getMessage(String code) {
        return errorMessages.getOrDefault(code, "Recurso no encontrado");
    }
}
