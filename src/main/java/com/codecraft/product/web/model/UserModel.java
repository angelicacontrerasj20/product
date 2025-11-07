package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo que representa los datos de un usuario.
 */
@Getter
@Setter
public class UserModel implements Serializable {
    private Long id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private LocalDate dateOfBirth;
    private String gender;
    private String stateOfBirth;
}
