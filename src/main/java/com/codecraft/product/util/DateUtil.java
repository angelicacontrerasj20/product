package com.codecraft.product.util;

import java.time.LocalDateTime;

public class DateUtil {
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String formatDateToIso(String fecha) {
        if (fecha != null && !fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return date.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                return "1900-01-01";
            }
        }
        return fecha;
    }

    /**
     * Convierte un LocalDateTime a java.sql.Timestamp
     * @param localDateTime Fecha y hora en formato LocalDateTime
     * @return Timestamp equivalente o null si el parámetro es null
     */
    public static java.sql.Timestamp toTimestamp(java.time.LocalDateTime localDateTime) {
        return localDateTime != null ? java.sql.Timestamp.valueOf(localDateTime) : null;
    }

    /**
     * Convierte cualquier objeto de fecha (Timestamp, LocalDateTime, null) a LocalDateTime.
     * Si el valor es null o no es compatible, retorna LocalDateTime por defecto.
     * @param dateObj Objeto de fecha
     * @return LocalDateTime equivalente o valor por defecto
     */
    public static LocalDateTime toLocalDateTime(Object dateObj) {
        if (dateObj instanceof java.sql.Timestamp ts) {
            return ts.toLocalDateTime();
        } else if (dateObj instanceof LocalDateTime ldt) {
            return ldt;
        } else {
            return LocalDateTime.of(1900, 1, 1, 0, 0);
        }
    }

    /**
     * Convierte cualquier objeto de fecha (Date, LocalDate, null) a LocalDate.
     * Si el valor es null o no es compatible, retorna LocalDate por defecto.
     * @param dateObj Objeto de fecha
     * @return LocalDate equivalente o valor por defecto
     */
    public static java.time.LocalDate toLocalDate(Object dateObj) {
        if (dateObj instanceof java.sql.Date d) {
            return d.toLocalDate();
        } else if (dateObj instanceof java.time.LocalDate ld) {
            return ld;
        } else {
            return java.time.LocalDate.of(1900, 1, 1);
        }
    }
}
