package com.codecraft.product.mapper;

import com.codecraft.product.util.DateUtil;
import com.codecraft.product.web.model.UserModel;
import java.util.Map;

/**
 * Mapper para convertir un Map<String, Object> en un UserModel.
 */
public class UserModelMapper {
    /**
     * Convierte un Map en un UserModel.
     * @param result Mapa con los datos del usuario
     * @return UserModel mapeado
     */
    public static UserModel map(Map<String, Object> result) {
        UserModel model = new UserModel();
        model.setId(result.get("usuario_id") != null ? ((Number) result.get("usuario_id")).longValue() : null);
        model.setUserName((String) result.get("nombre_usuario"));
        model.setPassword((String) result.get("contrasenia"));
        model.setFirstName((String) result.get("primer_nombre"));
        model.setLastName((String) result.get("apellido_paterno"));
        model.setMiddleName((String) result.get("apellido_materno"));
        Object fechaNacimientoObj = result.get("fecha_nacimiento");
        model.setDateOfBirth(DateUtil.toLocalDate(fechaNacimientoObj));
        model.setGender((String) result.get("genero"));
        model.setStateOfBirth((String) result.get("estado_nacimiento"));
        Object fechaRegistroObj = result.get("fecha_registro");
        model.setRegisterDate(DateUtil.toLocalDateTime(fechaRegistroObj));
        Object fechaActualizacionObj = result.get("fecha_actualizacion");
        model.setUpdateDate(DateUtil.toLocalDateTime(fechaActualizacionObj));
        return model;
    }
}

