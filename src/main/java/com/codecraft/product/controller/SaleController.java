package com.codecraft.product.controller;

/**
 * Controlador para gestionar las operaciones de ventas (Sale).
 * Expone endpoints para consultar ventas por usuario y otras operaciones relacionadas.
 */
import com.codecraft.product.service.SaleService;
import com.codecraft.product.web.model.ProductPurchaseModel;
import com.codecraft.product.web.model.SaleGetModel;
import com.codecraft.product.web.model.SaleModel;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @GetMapping("/user/{userId}")
    public List<SaleGetModel> getSalesByUser(@PathVariable Long userId) {
        return saleService.getSalesByUserId(userId);
    }

    /**
     * Endpoint para comprar múltiples productos en una sola transacción.
     * @param request Modelo con los datos de la compra múltiple.
     * @return Modelo de la venta realizada.
     */
    @PostMapping("/buy-multiple")
    @Operation(summary = "Venta de Productos", description = "Venta de productos por usuario.")
    public SaleModel buyMultipleProducts(@RequestBody ProductPurchaseModel request) {
        return saleService.buyMultipleProducts(request);
    }
}
