package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.User;
import com.codecraft.product.web.model.UserUpdateModel;
import com.codecraft.product.util.DateUtil;
import org.springframework.beans.BeanUtils;

/**
 * Conversor para actualización de entidades de usuario.
 */
public class UserUpdateConverter {
    /**
     * Actualiza una entidad User con los datos de un modelo UserUpdateModel.
     *
     * @param userModel Modelo UserUpdateModel.
     * @param user      Entidad User a actualizar.
     */
    public static void updateEntity(UserUpdateModel userModel, User user) {
        BeanUtils.copyProperties(userModel, user);
        user.setUpdateDate(DateUtil.now());
    }
}
