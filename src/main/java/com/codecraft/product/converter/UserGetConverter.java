package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.User;
import com.codecraft.product.web.model.UserGetModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

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
