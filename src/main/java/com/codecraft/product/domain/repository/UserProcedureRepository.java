package com.codecraft.product.domain.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Repositorio para ejecutar procedimientos almacenados relacionados con usuarios.
 * Permite la ejecución de procedimientos para alta, modificación y consulta de usuarios.
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

    /**
     * Ejecuta el procedimiento almacenado USUARIOMOD para modificar usuario.
     * @param inParams Mapa de parámetros de entrada
     * @return Mapa con el resultset del procedimiento
     */
    public Map<String, Object> modificarUsuario(Map<String, Object> inParams) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("USUARIOMOD");
        return jdbcCall.execute(inParams);
    }
}
