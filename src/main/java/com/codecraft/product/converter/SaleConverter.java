package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Sale;
import com.codecraft.product.web.model.SaleModel;
import com.codecraft.product.web.model.SaleItemModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

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
        BeanUtils.copyProperties(sale, model);
        model.setUserId(sale.getUser() != null ? sale.getUser().getId() : null);
        if (sale.getItems() != null) {
            List<SaleItemModel> items = sale.getItems().stream()
                .map(SaleItemConverter::toModel)
                .collect(Collectors.toList());
            model.setItems(items);
        }
        return model;
    }


}
