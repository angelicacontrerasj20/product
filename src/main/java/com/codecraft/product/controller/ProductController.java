package com.codecraft.product.controller;

import com.codecraft.product.service.ProductService;
import com.codecraft.product.web.model.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de productos y operaciones relacionadas.
 * Controlador para gestionar las operaciones de productos.
 * Expone endpoints para consulta, registro, actualización y eliminación de productos.
 */
@RestController
@RequestMapping("/products")
@Tag(name = "Producto", description = "Operaciones relacionadas con productos")
public class ProductController {
    @Autowired
    private ProductService productService;

    /**
     * Endpoint para agregar un nuevo producto.
     * @param product Modelo del producto a agregar.
     * @return Modelo con los datos del producto agregado.
     */
    @PostMapping("/new")
    @Operation(summary = "Crear un nuevo producto", description = "Registro de producto con su inventario inicial.")
    public ProductAddModel addProduct(@RequestBody ProductModel product) {
        return productService.addProduct(product);
    }

    /**
     * Endpoint para listar todos los productos activos con inventario.
     * @return Lista de productos con inventario.
     */
    @GetMapping
    @Operation(summary = "Consultar lista de productos", description = "Consulta filtrada por productos activos o inactivos.")
    public List<ProductAddModel> listProducts(@RequestParam(name = "active", required = false) Boolean active) {
        return productService.listProducts(active);
    }

    /**
     * Endpoint para buscar un producto por su ID.
     * @param id ID del producto.
     * @return Optional con el modelo del producto si existe.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar producto individual", description = "Consulta por ID del producto.")
    public Optional<ProductAddModel> findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    /**
     * Endpoint para actualizar el estado activo/inactivo de un producto por ID.
     * @param id ID del producto.
     * @param activeStatus Modelo con el nuevo estado activo.
     * @return Modelo actualizado del producto.
     */
    @PatchMapping("/{id}/active")
    @Operation(summary = "Actualizar estado activo", description = "Actualiza el estado activo/inactivo del producto por ID.")
    public ProductModel updateActiveStatus(@PathVariable Long id, @RequestBody ProductPatchModel activeStatus) {
        return productService.updateProductActiveStatus(id, activeStatus.getActive());
    }


    /**
     * Endpoint para actualizar todos los datos de un producto por ID.
     * @param id ID del producto.
     * @param productModel Modelo con los nuevos datos.
     * @return Modelo actualizado del producto.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar el producto", description = "Actualización de descripcion, stock, precio y si se encuentra activo.")
    public ProductAddModel updateProduct(@PathVariable Long id, @RequestBody ProductUpdateModel productModel) {
        return productService.updateProduct(id, productModel);
    }

    /**
     * Endpoint para eliminar un producto y su inventario por ID.
     * @param id ID del producto.
     * @return ResponseEntity con status 204 si se elimina correctamente.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina un producto y su inventario por ID.")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
