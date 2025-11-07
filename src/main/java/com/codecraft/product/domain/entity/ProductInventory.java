package com.codecraft.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "productoinventario")
public class ProductInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_inventario_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Product product;

    @Column(name = "cantidad", nullable = false)
    private int quantity;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime registerDate;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime updateDate;
}
