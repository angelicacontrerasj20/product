package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.domain.entity.SaleItem;
import com.codecraft.product.web.model.SaleItemGetModel;

/**
 * Convierte entidades SaleItem en modelos SaleItemGetModel y viceversa.
 * Utilizado para mapear los items de una venta en la capa de servicio y controlador.
 */
public class SaleItemGetConverter {
    public static SaleItemGetModel toModel(SaleItem item) {
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
