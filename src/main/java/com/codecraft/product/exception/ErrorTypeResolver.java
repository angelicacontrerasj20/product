package com.codecraft.product.exception;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utilidad para resolver el tipo de error y mensaje dinámico según el mensaje de excepción.
 */
public class ErrorTypeResolver {
    private static final Map<String, ErrorType> ERROR_PATTERNS = new LinkedHashMap<>();
    static {
        ERROR_PATTERNS.put("Duplicate entry", new ErrorType("DUPLICATE_ENTRY", "El registro ya existe"));
        ERROR_PATTERNS.put("not-null property", new ErrorType("REQUIRED_FIELD_MISSING", "Campo requerido faltante"));
        ERROR_PATTERNS.put("foreign key constraint fails", new ErrorType("FOREIGN_KEY_CONSTRAINT", "Violación de restricción de clave foránea"));
        // Puedes agregar más patrones aquí
    }

    public static ErrorType resolve(String exceptionMessage) {
        if (exceptionMessage == null) {
            return new ErrorType("DATA_INTEGRITY_VIOLATION", "Violación de integridad de datos");
        }
        for (Map.Entry<String, ErrorType> entry : ERROR_PATTERNS.entrySet()) {
            if (exceptionMessage.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return new ErrorType("DATA_INTEGRITY_VIOLATION", "Violación de integridad de datos");
    }

    public static class ErrorType {
        public final String code;
        public final String message;
        public ErrorType(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
