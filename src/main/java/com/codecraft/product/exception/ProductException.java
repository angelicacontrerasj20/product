package com.codecraft.product.exception;

/**
 * Excepción personalizada para errores de producto.
 * Se lanza cuando ocurre un error específico en operaciones de producto.
 */
public class ProductException extends RuntimeException {
    private final String errorCode;

    public ProductException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
