package com.codecraft.product.web.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductUpdateModel implements Serializable {
    private Long id;
    private String description;
    private BigDecimal price;
    private int stock;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private Boolean active;
}
