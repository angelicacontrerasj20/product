package com.codecraft.product.converter;

import com.codecraft.product.exception.ErrorCode;
import com.codecraft.product.exception.ResourceNotFoundGlobalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Convierte el resultado de un procedimiento almacenado de usuario a UserModel o a un Map de error.
 */
public class ProcedureResultAddConverter {
    private static final Logger logger = LogManager.getLogger(ProcedureResultAddConverter.class);

    /**
     * Convierte el resultado de un procedimiento almacenado (Map directo o resultset) a cualquier modelo usando un convertidor.
     * Si hay error, lanza excepción personalizada.
     * @param result Resultado del procedimiento (Map<String, Object> o con #result-set-1)
     * @param idField Nombre del campo clave (por ejemplo, "usuario_id")
     * @param converter Función que convierte el Map en el modelo deseado
     * @param <T> Tipo de modelo a retornar
     * @return Modelo si no hay error, lanza excepción si hay error
     */
    public static <T> T convert(Map<String, Object> result, String idField, Function<Map<String, Object>, T> converter) {
        Map<String, Object> row = result;
        if (result.containsKey("#result-set-1")) {
            List<Map<String, Object>> rs = (List<Map<String, Object>>) result.get("#result-set-1");
            if (!rs.isEmpty()) {
                row = rs.get(0);
            } else {
                throw new ResourceNotFoundGlobalException(ErrorCode.GENERAL_ERROR);
            }
        }
        if (row.containsKey("Err_Codigo") && row.get("Err_Codigo") != null) {
            String codigoError = String.valueOf(row.get("Err_Codigo"));
            String mensajeError = String.valueOf(row.get("Err_Mensaj"));
            logger.error("Error en alta de entidad: {} - {}", codigoError, mensajeError);
            throw new ResourceNotFoundGlobalException(codigoError);
        }
        if (row.containsKey(idField)) {
            return converter.apply(row);
        }
        throw new ResourceNotFoundGlobalException(ErrorCode.GENERAL_ERROR);
    }
}
