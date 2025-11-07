package com.codecraft.product.service;


import com.codecraft.product.domain.entity.ProductInventory;
import com.codecraft.product.domain.repository.ProductInventoryRepository;
import com.codecraft.product.web.model.ProductInventoryModel;
import com.codecraft.product.converter.ProductInventoryConverter;
import com.codecraft.product.domain.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servicio para la gestión de inventario de productos.
 */
@Service
public class ProductInventoryService {
    private static final Logger logger = LogManager.getLogger(ProductInventoryService.class);

    @Autowired
    private ProductInventoryRepository productInventoryRepository;

    /**
     * Lista todos los inventarios existentes.
     * @return Lista de modelos de inventario.
     */
    public List<ProductInventoryModel> listInventory() {
        logger.info("Listando inventario de productos");
        return productInventoryRepository.findAll().stream()
            .map(ProductInventoryConverter::toModel)
            .toList();
    }

    /**
     * Busca el inventario por el ID del producto.
     * @param id ID del producto.
     * @return Optional con el modelo de inventario si existe.
     */
    public Optional<ProductInventoryModel> findById(Long id) {
        logger.info("Buscando inventario por ID: {}", id);
        return productInventoryRepository.findById(id)
            .map(ProductInventoryConverter::toModel);
    }

    /**
     * Busca el inventario por el ID del producto y retorna el modelo directamente.
     * @param productId ID del producto.
     * @return Modelo de inventario si existe, null si no.
     */
    public ProductInventoryModel findByProductId(Long productId) {
        logger.info("Buscando inventario por ID de producto: {}", productId);
        return productInventoryRepository.findAll().stream()
            .filter(inv -> inv.getProduct().getId().equals(productId))
            .findFirst()
            .map(ProductInventoryConverter::toModel)
            .orElse(null);
    }

    /**
     * Guarda o actualiza el inventario de un producto.
     * @param inventoryModel Modelo de inventario.
     * @param product Producto asociado.
     * @return Modelo actualizado de inventario.
     */
    public ProductInventoryModel saveInventory(ProductInventoryModel inventoryModel, Product product) {
        logger.info("Guardando inventario para producto ID: {}", product.getId());
        ProductInventory entity = ProductInventoryConverter.toEntity(inventoryModel, product);
        ProductInventory saved = productInventoryRepository.save(entity);
        return ProductInventoryConverter.toModel(saved);
    }

    /**
     * Elimina el inventario asociado a un producto por su ID.
     * @param productId ID del producto.
     */
    public void deleteByProductId(Long productId) {
        logger.info("Eliminando inventario para producto ID: {}", productId);
        productInventoryRepository.deleteByProductId(productId);
    }

}
