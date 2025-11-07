package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UserUpdatePasswordModel implements Serializable {

    private String currentPassword;
    private String newPassword;
}
