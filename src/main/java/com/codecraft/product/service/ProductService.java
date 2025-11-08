package com.codecraft.product.service;

import com.codecraft.product.converter.ProductAddConverter;
import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.domain.repository.ProductRepository;
import com.codecraft.product.exception.ErrorCode;
import com.codecraft.product.exception.ResourceNotFoundGlobalException;
import com.codecraft.product.util.DateUtil;
import com.codecraft.product.web.model.*;
import com.codecraft.product.converter.ProductConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import com.codecraft.product.domain.repository.SaleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.codecraft.product.util.TraceIdUtil;

@Service
public class ProductService {
    private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInventoryService productInventoryService;
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserService userService;

    /**
     * Agrega un nuevo producto y su inventario al sistema.
     * @param productModel Modelo con los datos del producto.
     * @return Modelo con los datos del producto y su inventario.
     */
    public ProductAddModel addProduct(ProductModel productModel) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.addProduct " + traceId + " " + productModel.getName());
        try {
            Product product = ProductConverter.toEntity(productModel);
            product.setRegisterDate(DateUtil.now());
            product.setUpdateDate(DateUtil.now());
            Product newProduct = productRepository.save(product);
            ProductInventoryModel model = new ProductInventoryModel();
            model.setProductId(newProduct.getId());
            model.setQuantity(productModel.getStock());
            model.setRegisterDate(DateUtil.now());
            model.setUpdateDate(DateUtil.now());
            ProductInventoryModel productInventory = productInventoryService.saveInventory(model, newProduct);
            return ProductAddConverter.toModel(newProduct, productInventory);
        } catch (DataIntegrityViolationException ex) {
            logger.error("ProductService.addProduct " + traceId + " Error de integridad de datos al agregar producto: {}", productModel.getName(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_DUPLICATE);
        } catch (Exception ex) {
            logger.error("ProductService.addProduct " + traceId + " Error al agregar producto: {}", productModel.getName(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    /**
     * Lista productos filtrados por estado (activo/inactivo) junto con su inventario.
     * @param active true para activos, false para inactivos, null para todos
     * @return Lista de modelos de productos con inventario.
     */
    public List<ProductAddModel> listProducts(Boolean active) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.listProducts " + traceId + " " + active);
        List<Product> products = productRepository.findAll();
        List<ProductAddModel> result = new ArrayList<>();
        for (Product product : products) {
            if (active == null || product.getActive().equals(active)) {
                ProductInventoryModel inventoryModel = productInventoryService.findByProductId(product.getId());
                result.add(ProductAddConverter.toModel(product, inventoryModel));
            }
        }
        return result;
    }

    /**
     * Busca un producto por su ID y retorna el modelo con inventario.
     * @param id ID del producto.
     * @return Optional con el modelo del producto y su inventario si existe.
     */
    public Optional<ProductAddModel> findById(Long id) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.findById " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            ProductInventoryModel inventoryModel = productInventoryService.findByProductId(product.getId());
            return Optional.of(ProductAddConverter.toModel(product, inventoryModel));
        }
        return Optional.empty();
    }

    /**
     * Busca un producto por su ID y retorna solo el modelo ProductModel.
     * @param id ID del producto.
     * @return Modelo del producto si existe.
     */
    public Product findProductModelById(Long id) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.findProductModelById " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            return productOpt.get();
        }
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    /**
     * Actualiza el precio de un producto por su ID.
     * @param id ID del producto.
     * @param productPatchModel Modelo con el nuevo precio.
     * @return Modelo actualizado del producto.
     */
    public ProductModel updateProductDescription(Long id, ProductPatchModel productPatchModel) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.updateProductDescription " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setActive(productPatchModel.getActive());
            product.setUpdateDate(DateUtil.now());
            Product updated = productRepository.save(product);
            return ProductConverter.toModel(updated);
        }
        logger.error("ProductService.updateProductDescription " + traceId + " Producto no encontrado para actualizar precio. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    /**
     * Actualiza todos los datos de un producto por su ID.
     * @param id ID del producto.
     * @param productModel Modelo con los nuevos datos.
     * @return Modelo actualizado del producto.
     */
    public ProductAddModel updateProduct(Long id, ProductUpdateModel productModel) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.updateProduct " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setDescription(productModel.getDescription());
            product.setPrice(productModel.getPrice());
            product.setActive(productModel.getActive());
            product.setUpdateDate(DateUtil.now());
            Product updated = productRepository.save(product);
            // Actualizar el stock en ProductInventory
            ProductInventoryModel inventoryModel = productInventoryService.findByProductId(product.getId());
            if (Objects.nonNull(inventoryModel)) {
                inventoryModel.setQuantity(productModel.getStock());
                inventoryModel.setUpdateDate(DateUtil.now());
                productInventoryService.saveInventory(inventoryModel, updated);
            }
            return ProductAddConverter.toModel(updated, inventoryModel);
        }
        logger.error("ProductService.updateProduct " + traceId + " Producto no encontrado para actualizar. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    /**
     * Elimina un producto y su inventario por ID.
     * @param id ID del producto a eliminar.
     */
    @Transactional
    public void deleteProduct(Long id) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.deleteProduct " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            // Eliminar inventario asociado si existe
            productInventoryService.deleteByProductId(product.getId());
            // Eliminar el producto
            productRepository.delete(product);
        } else {
            logger.error("ProductService.deleteProduct " + traceId + " Producto no encontrado para eliminar. ID: {}", id);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    /**
     * Actualiza el estado activo/inactivo de un producto por su ID.
     * @param id ID del producto.
     * @param active Nuevo estado activo/inactivo.
     * @return Modelo actualizado del producto.
     */
    public ProductModel updateProductActiveStatus(Long id, Boolean active) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.updateProductActiveStatus " + traceId + " " + id + " " + active);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setActive(active);
            product.setUpdateDate(DateUtil.now());
            Product updated = productRepository.save(product);
            return ProductConverter.toModel(updated);
        }
        logger.error("ProductService.updateProductActiveStatus " + traceId + " Producto no encontrado para actualizar estado activo. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    public Product getProductEntityById(Long id) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("ProductService.getProductEntityById " + traceId + " " + id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            return productOpt.get();
        }
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

}
