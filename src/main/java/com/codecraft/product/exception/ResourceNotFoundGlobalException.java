package com.codecraft.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para errores de recursos no encontrados.
 * Se lanza cuando no se encuentra una entidad solicitada en la base de datos.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundGlobalException extends RuntimeException {
    private final String errorCode;

    public ResourceNotFoundGlobalException(String errorCode) {
        super(ErrorCode.getMessage(errorCode));
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
