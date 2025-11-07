package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Modelo para actualizar los datos de un usuario.
 */
@Getter
@Setter
public class UserUpdateModel implements Serializable {
        private Long id;
        private String firstName;
        private String lastName;
        private String middleName;
        private LocalDate dateOfBirth;
        private String gender;
        private String stateOfBirth;
}
