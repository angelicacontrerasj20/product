package com.codecraft.product.controller;

import com.codecraft.product.service.ProductInventoryService;
import com.codecraft.product.web.model.ProductInventoryModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de inventario de productos.
 */
@RestController
@RequestMapping("/inventory")
@Tag(name = "Producto Inventario", description = "Operaciones relacionadas con inventario del productp")
public class ProductInventoryController {
    @Autowired
    private ProductInventoryService productInventoryService;

    /**
     * Endpoint para listar todos los inventarios de productos.
     * @return Lista de inventarios.
     */
    @GetMapping
    @Operation(summary = "Consultar lista de inventario de productos", description = "Consulta de todo el inventario.")
    public List<ProductInventoryModel> listInventories() {
        return productInventoryService.listInventory();
    }

    /**
     * Endpoint para buscar el inventario por ID de producto.
     * @param id ID del producto.
     * @return Optional con el modelo de inventario si existe.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consulta de inventario individual", description = "Consulta por inventario ID.")
    public Optional<ProductInventoryModel> findById(@PathVariable Long id) {
        return productInventoryService.findById(id);
    }

}