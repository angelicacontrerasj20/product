package com.codecraft.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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
