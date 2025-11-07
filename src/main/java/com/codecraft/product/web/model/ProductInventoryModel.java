package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa el inventario de un producto.
 */
@Getter
@Setter
public class ProductInventoryModel implements Serializable {
    private Long id;
    private Long productId;
    private int quantity;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
}

