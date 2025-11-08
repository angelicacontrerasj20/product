package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.User;
import com.codecraft.product.web.model.UserGetModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Convierte entidades User en modelos UserGetModel para respuestas sin contraseña.
 * Utilizado en endpoints que no deben exponer la contraseña del usuario.
 */
@Component
public class UserGetConverter {

    /**
     * Convierte una entidad User en un modelo UserModel.
     * @param user Entidad User.
     * @return Modelo UserModel.
     */
    public static UserGetModel entityToModel(User user) {
        UserGetModel userModel = new UserGetModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }
}
