package com.codecraft.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entidad que representa a un usuario en la base de datos.
 * Incluye datos personales, credenciales y relaciones con ventas y auditoría.
 */
@Getter
@Setter
@Entity
@Table(name = "usuario",
    uniqueConstraints = {@UniqueConstraint(columnNames = "nombre_usuario")},
    indexes = {
        @Index(name = "idx_usuario_nombre_usuario", columnList = "nombre_usuario")
    }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nombre_usuario", length = 15, nullable = false, unique = true)
    private String userName;

    @Column(name = "contrasenia", length = 50, nullable = false)
    private String password;

    @Column(name = "primer_nombre", length = 50, nullable = false)
    private String firstName;

    @Column(name = "apellido_paterno", length = 50, nullable = false)
    private String lastName;

    @Column(name = "apellido_materno", length = 50, nullable = false)
    private String middleName;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "genero", length = 1, nullable = false)
    private String gender;

    @Column(name = "estado_nacimiento",length = 50, nullable = false)
    private String stateOfBirth;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime registerDate;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateDate;
}
