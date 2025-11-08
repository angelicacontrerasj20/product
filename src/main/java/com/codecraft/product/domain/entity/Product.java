package com.codecraft.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "producto",
       indexes = {
           @Index(name = "idx_producto_nombre", columnList = "nombre"),
           @Index(name = "idx_producto_activo", columnList = "activo")
       })
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "descripcion",length = 500, nullable = false)
    private String description;

    @Column(name = "precio", nullable = false)
    private BigDecimal price;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime registerDate;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateDate;

    @Column(name = "activo", nullable = false)
    private Boolean active;
}
