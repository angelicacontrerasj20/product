package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.User;
import com.codecraft.product.util.PasswordUtil;
import com.codecraft.product.web.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Conversor para entidades y modelos de usuario.
 */
@Component
public class UserConverter {

    /**
     * Convierte una entidad User en un modelo UserModel.
     * @param user Entidad User.
     * @return Modelo UserModel.
     */
    public UserModel entityToModel(User user) {
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(user, userModel);
        return userModel;
    }

    /**
     * Convierte un modelo UserModel en una entidad User.
     * @param userModel Modelo UserModel.
     * @return Entidad User.
     */
    public static User modelToEntity(UserModel userModel) {
        User user = new User();
        BeanUtils.copyProperties(userModel, user);
        return user;
    }
}
