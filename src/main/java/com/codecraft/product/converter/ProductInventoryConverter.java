package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.ProductInventory;
import com.codecraft.product.web.model.ProductInventoryModel;
import com.codecraft.product.domain.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * Conversor para entidades y modelos de inventario de producto.
 */
public class ProductInventoryConverter {
    /**
     * Convierte una entidad ProductInventory en un modelo ProductInventoryModel.
     * @param entity Entidad ProductInventory.
     * @return Modelo ProductInventoryModel.
     */
    public static ProductInventoryModel toModel(ProductInventory entity) {
        ProductInventoryModel model = new ProductInventoryModel();
        BeanUtils.copyProperties(entity, model);
        // Asignar manualmente el productId
        if (entity.getProduct() != null) {
            model.setProductId(entity.getProduct().getId());
        }
        return model;
    }

    /**
     * Convierte un modelo ProductInventoryModel y un producto en una entidad ProductInventory.
     * @param model Modelo ProductInventoryModel.
     * @param product Entidad Product asociada.
     * @return Entidad ProductInventory.
     */
    public static ProductInventory toEntity(ProductInventoryModel model, Product product) {
        ProductInventory entity = new ProductInventory();
        BeanUtils.copyProperties(model, entity);
        entity.setProduct(product);
        return entity;
    }
}