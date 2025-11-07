package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Modelo que representa un ítem de compra de producto.
 */
@Getter
@Setter
public class ProductPurchaseItemModel implements Serializable{
    private Long productId;
    private int quantity;
}
