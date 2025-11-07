package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Sale;
import com.codecraft.product.domain.entity.SaleItem;
import com.codecraft.product.web.model.SaleModel;
import com.codecraft.product.web.model.SaleItemModel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Conversor para entidades y modelos de venta.
 */
public class SaleConverter {
    /**
     * Convierte una entidad Sale en un modelo SaleModel.
     * @param sale Entidad Sale.
     * @return Modelo SaleModel.
     */
    public static SaleModel toModel(Sale sale) {
        SaleModel model = new SaleModel();
        model.setId(sale.getId());
        model.setUserId(sale.getUser() != null ? sale.getUser().getId() : null);
        model.setDate(sale.getDate());
        model.setTotalPrice(sale.getTotalPrice());
        if (sale.getItems() != null) {
            List<SaleItemModel> items = sale.getItems().stream()
                .map(SaleConverter::toItemModel)
                .collect(Collectors.toList());
            model.setItems(items);
        }
        return model;
    }

    /**
     * Convierte una entidad SaleItem en un modelo SaleItemModel.
     * @param item Entidad SaleItem.
     * @return Modelo SaleItemModel.
     */
    public static SaleItemModel toItemModel(SaleItem item) {
        SaleItemModel model = new SaleItemModel();
        model.setProductId(item.getProduct() != null ? item.getProduct().getId() : null);
        model.setQuantity(item.getQuantity());
        model.setUnitPrice(item.getUnitPrice());
        return model;
    }
}
