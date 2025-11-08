package com.codecraft.product.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * Utilidad para gestionar Trace IDs en solicitudes HTTP.
 * Proporciona métodos para obtener o crear un Trace ID único por solicitud.
 * Utilizado para rastrear y correlacionar solicitudes en sistemas distribuidos.
 */
public class TraceIdUtil {

    /** Obtiene el Trace ID de la solicitud actual o crea uno nuevo si no existe.
     * @return Trace ID único para la solicitud.
     */
    public static String getOrCreateTraceId() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        String traceId = null;
        if (requestAttributes != null) {
            traceId = (String) requestAttributes.getAttribute("traceId", RequestAttributes.SCOPE_REQUEST);
            if (traceId == null) {
                traceId = generateTraceId();
                requestAttributes.setAttribute("traceId", traceId, RequestAttributes.SCOPE_REQUEST);
            }
        } else {
            traceId = generateTraceId();
        }
        return traceId;
    }

    /** Genera un nuevo Trace ID único.
     * @return Nuevo Trace ID.
     */
    public static String generateTraceId() {
        return java.util.UUID.randomUUID().toString();
    }
}

