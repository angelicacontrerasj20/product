package com.codecraft.product.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Repository para procedimientos almacenados de usuario.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@Repository
public class UserProcedureRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Ejecuta el procedimiento almacenado USUARIOALT para alta de usuario.
     * @param inParams Mapa de parámetros de entrada
     * @return Mapa con el resultset del procedimiento
     */
    public Map<String, Object> altaUsuario(Map<String, Object> inParams) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("USUARIOALT");
        return jdbcCall.execute(inParams);
    }
}
