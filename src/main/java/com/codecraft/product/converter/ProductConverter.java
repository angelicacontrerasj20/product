package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.web.model.ProductModel;
import org.springframework.beans.BeanUtils;

/**
 * Conversor para entidades y modelos de producto.
 */
public class ProductConverter {

    /**
     * Convierte una entidad Product en un modelo ProductModel.
     * @param entity Entidad Product.
     * @return Modelo ProductModel.
     */
    public static ProductModel toModel(Product entity) {
        ProductModel model = new ProductModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    /**
     * Convierte un modelo ProductModel en una entidad Product.
     * @param model Modelo ProductModel.
     * @return Entidad Product.
     */
    public static Product toEntity(ProductModel model) {
        Product entity = new Product();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

}
