package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class SaleItemGetModel implements Serializable {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;

}

