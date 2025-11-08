package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class SaleGetModel implements Serializable {
    private Long id;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private Long userId;
    private List<SaleItemGetModel> items;
}

