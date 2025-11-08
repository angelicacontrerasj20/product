package com.codecraft.product.service;


import com.codecraft.product.domain.entity.ProductInventory;
import com.codecraft.product.domain.repository.ProductInventoryRepository;
import com.codecraft.product.web.model.ProductInventoryModel;
import com.codecraft.product.converter.ProductInventoryConverter;
import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.util.TraceIdUtil;
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
     * Lista todos los inventarios existentes en la base de datos y los convierte a modelos.
     * @return Lista de modelos de inventario.
     */
    public List<ProductInventoryModel> listInventory() {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductInventoryService.listInventory " + traceId);
        return productInventoryRepository.findAll().stream()
            .map(ProductInventoryConverter::toModel)
            .toList();
    }

    /**
     * Busca el inventario por el ID del producto en la base de datos y lo convierte a modelo.
     * @param id ID del producto.
     * @return Optional con el modelo de inventario si existe.
     */
    public Optional<ProductInventoryModel> findById(Long id) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductInventoryService.findById " + traceId + " " + id);
        return productInventoryRepository.findById(id)
            .map(ProductInventoryConverter::toModel);
    }

    /**
     * Busca el inventario por el ID del producto y retorna el modelo directamente, o null si no existe.
     * @param productId ID del producto.
     * @return Modelo de inventario si existe, null si no.
     */
    public ProductInventoryModel findByProductId(Long productId) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductInventoryService.findByProductId " + traceId + " " + productId);
        return productInventoryRepository.findAll().stream()
            .filter(inv -> inv.getProduct().getId().equals(productId))
            .findFirst()
            .map(ProductInventoryConverter::toModel)
            .orElse(null);
    }

    /**
     * Guarda o actualiza el inventario de un producto en la base de datos.
     * @param inventoryModel Modelo de inventario.
     * @param product Producto asociado.
     * @return Modelo actualizado de inventario.
     */
    public ProductInventoryModel saveInventory(ProductInventoryModel inventoryModel, Product product) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductInventoryService.saveInventory " + traceId + " " + inventoryModel.getProductId());
        ProductInventory entity = ProductInventoryConverter.toEntity(inventoryModel, product);
        ProductInventory saved = productInventoryRepository.save(entity);
        return ProductInventoryConverter.toModel(saved);
    }

    /**
     * Elimina el inventario asociado a un producto por su ID en la base de datos.
     * @param productId ID del producto.
     */
    public void deleteByProductId(Long productId) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductInventoryService.deleteByProductId " + traceId + " " + productId);
        productInventoryRepository.deleteByProductId(productId);
    }

}
