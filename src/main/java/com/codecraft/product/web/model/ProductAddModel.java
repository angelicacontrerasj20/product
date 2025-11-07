package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Modelo que representa un producto junto con su inventario.
 */
@Getter
@Setter
public class ProductAddModel implements Serializable {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductInventoryModel productInventoryModel;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private Boolean active;
}
