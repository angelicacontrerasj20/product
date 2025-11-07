package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Modelo que representa una compra múltiple de productos.
 */
@Getter
@Setter
public class ProductPurchaseModel implements Serializable{
    private Long userId;
    private List<ProductPurchaseItemModel> items;
}
