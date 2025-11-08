package com.codecraft.product.converter;

import com.codecraft.product.domain.entity.Sale;
import com.codecraft.product.domain.entity.User;
import com.codecraft.product.web.model.SaleGetModel;
import com.codecraft.product.web.model.SaleItemGetModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

public class SaleGetModelConverter {
    public static SaleGetModel toModel(Sale sale) {
        SaleGetModel model = new SaleGetModel();
        BeanUtils.copyProperties(sale, model);
        model.setUserId(sale.getUser() != null ? sale.getUser().getId() : null);
        if (sale.getItems() != null) {
            List<SaleItemGetModel> items = sale.getItems().stream()
                .map(SaleItemGetConverter::toModel)
                .collect(Collectors.toList());
            model.setItems(items);
        }
        return model;
    }

    public static Sale toEntity(SaleGetModel model, User user) {
        Sale sale = new Sale();
        BeanUtils.copyProperties(model, sale);
        sale.setUser(user);
        // Los SaleItem deben ser convertidos y asociados por fuera, ya que requieren Product y Sale
        return sale;
    }
}
