package com.codecraft.product.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador de excepciones específicas del dominio de productos
 * Esta clase maneja solo las excepciones personalizadas de la aplicación
 */
@RestControllerAdvice
public class ProductExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductExceptionHandler.class);

    /**
     * Maneja la excepción ResourceNotFoundGlobalException
     */
    @ExceptionHandler(ResourceNotFoundGlobalException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundGlobalException ex) {
        logger.error("Recurso no encontrado: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja la excepción ProductException
     */
    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorResponse> handleProductException(ProductException ex) {
        logger.error("Error de producto: {}", ex.getMessage());

        ErrorResponse error = new ErrorResponse(
            ex.getErrorCode(),
            ex.getMessage(),
            HttpStatus.BAD_REQUEST.value()
        );
        return ResponseEntity.badRequest().body(error);
    }
}
