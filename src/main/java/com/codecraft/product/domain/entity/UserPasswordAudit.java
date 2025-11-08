package com.codecraft.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuario_auditoria",
       indexes = {
           @Index(name = "idx_auditoria_usuario_id", columnList = "usario_id")
       })
public class UserPasswordAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auditoria_id", nullable = false)
    private Integer auditId;

    @Column(name = "usario_id", nullable = false)
    private Integer userId;

    @Column(name = "contrasenia_anterior")
    private String oldPassword;

    @Column(name = "contrasenia_nueva")
    private String newPassword;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime changedAt;

}
