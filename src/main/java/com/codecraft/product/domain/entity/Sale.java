package com.codecraft.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "venta",
       indexes = {
           @Index(name = "idx_venta_usuario_id", columnList = "usuario_id"),
           @Index(name = "idx_venta_fecha", columnList = "fecha")
       })
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private User user;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime date;

    @Column(name = "precio_total", nullable = false)
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleItem> items;

}
