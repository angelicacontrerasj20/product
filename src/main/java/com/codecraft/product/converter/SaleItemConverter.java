package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.domain.entity.SaleItem;
import com.codecraft.product.web.model.SaleItemGetModel;
import com.codecraft.product.web.model.SaleItemModel;
import org.springframework.beans.BeanUtils;

public class SaleItemConverter {
    public static SaleItemModel toModel(SaleItem item) {
        SaleItemModel model = new SaleItemModel();
        BeanUtils.copyProperties(item, model);
        model.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        return model;
    }

    public static SaleItemGetModel toGetModel(SaleItem item) {
        SaleItemGetModel model = new SaleItemGetModel();
        model.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        model.setProductName(item.getProduct() != null ? item.getProduct().getName() : null);
        model.setQuantity(item.getQuantity());
        model.setUnitPrice(item.getUnitPrice());
        return model;
    }

    public static SaleItem toEntity(SaleItemGetModel model, Product product) {
        SaleItem entity = new SaleItem();
        entity.setProduct(product);
        entity.setQuantity(model.getQuantity());
        entity.setUnitPrice(model.getUnitPrice());
        return entity;
    }
}
