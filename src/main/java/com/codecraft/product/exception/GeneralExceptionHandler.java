package com.codecraft.product.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Usa configuración específica para evitar conflictos con Swagger
 */
@ControllerAdvice(basePackages = {"com.codecraft.product.controller"})
@Order(1000) // Baja prioridad para evitar interferir con springdoc-openapi
public class GeneralExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    /**
     * Maneja violaciones de integridad de datos específicamente para controladores de producto
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        logger.error("Violación de integridad de datos: {}", ex.getMessage());
        ErrorTypeResolver.ErrorType errorType = ErrorTypeResolver.resolve(ex.getMessage());
        ErrorResponse error = new ErrorResponse(
            errorType.code,
            errorType.message,
            HttpStatus.CONFLICT.value()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
