package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modelo que representa una venta realizada.
 */
@Getter
@Setter
public class SaleModel implements Serializable {
    private Long id;
    private Long userId;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private List<SaleItemModel> items;
}