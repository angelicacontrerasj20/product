package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modelo que representa un ítem de venta de producto.
 */
@Getter
@Setter
public class SaleItemModel implements Serializable {
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;

}


