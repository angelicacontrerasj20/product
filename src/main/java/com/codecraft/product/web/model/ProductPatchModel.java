package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modelo para actualizar parcialmente los datos de un producto.
 */
@Getter
@Setter
public class ProductPatchModel implements Serializable {
    private BigDecimal price;

}
