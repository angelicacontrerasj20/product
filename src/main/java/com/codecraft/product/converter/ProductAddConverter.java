package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.web.model.ProductAddModel;
import com.codecraft.product.web.model.ProductInventoryModel;
import org.springframework.beans.BeanUtils;

/**
 * Conversor para modelos de producto con inventario.
 */
public class ProductAddConverter {
    /**
     * Convierte un producto y su inventario en un modelo combinado.
     *
     * @param product Entidad Product.
     * @param inventoryModel Modelo de inventario.
     * @return Modelo combinado de producto e inventario.
     */
    public static ProductAddModel toModel(Product product, ProductInventoryModel inventoryModel) {
        ProductAddModel model = new ProductAddModel();
        BeanUtils.copyProperties(product, model);
        model.setProductInventoryModel(inventoryModel);
        return model;
    }
}
