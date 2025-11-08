package com.codecraft.product.service;

import com.codecraft.product.converter.ProductAddConverter;
import com.codecraft.product.converter.UserConverter;
import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.domain.repository.ProductRepository;
import com.codecraft.product.exception.ErrorCode;
import com.codecraft.product.exception.ResourceNotFoundGlobalException;
import com.codecraft.product.exception.ProductException;
import com.codecraft.product.util.Constantes;
import com.codecraft.product.util.DateUtil;
import com.codecraft.product.web.model.*;
import com.codecraft.product.converter.ProductConverter;
import com.codecraft.product.converter.SaleConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import com.codecraft.product.domain.entity.Sale;
import com.codecraft.product.domain.entity.SaleItem;
import com.codecraft.product.domain.entity.User;
import com.codecraft.product.domain.repository.SaleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        logger.info("Agregando nuevo producto: {}", productModel.getName());
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
            logger.error("Error de integridad de datos al agregar producto: {}", productModel.getName(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_DUPLICATE);
        } catch (Exception ex) {
            logger.error("Error al agregar producto: {}", productModel.getName(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    /**
     * Lista productos filtrados por estado (activo/inactivo) junto con su inventario.
     * @param active true para activos, false para inactivos, null para todos
     * @return Lista de modelos de productos con inventario.
     */
    public List<ProductAddModel> listProducts(Boolean active) {
        logger.info("Listando productos con filtro activo: {}", active);
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
        logger.info("Buscando producto por ID: {}", id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            ProductInventoryModel inventoryModel = productInventoryService.findByProductId(product.getId());
            return Optional.of(ProductAddConverter.toModel(product, inventoryModel));
        }
        return Optional.empty();
    }

    /**
     * Actualiza el precio de un producto por su ID.
     * @param id ID del producto.
     * @param productPatchModel Modelo con el nuevo precio.
     * @return Modelo actualizado del producto.
     */
    public ProductModel updateProductDescription(Long id, ProductPatchModel productPatchModel) {
        logger.info("Actualizando precio de producto ID: {}", id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setActive(productPatchModel.getActive());
            product.setUpdateDate(DateUtil.now());
            Product updated = productRepository.save(product);
            return ProductConverter.toModel(updated);
        }
        logger.error("Producto no encontrado para actualizar precio. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    /**
     * Actualiza todos los datos de un producto por su ID.
     * @param id ID del producto.
     * @param productModel Modelo con los nuevos datos.
     * @return Modelo actualizado del producto.
     */
    public ProductAddModel updateProduct(Long id, ProductModel productModel) {
        logger.info("Actualizando producto ID: {}", id);
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
        logger.error("Producto no encontrado para actualizar. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

    /**
     * Realiza la compra múltiple de productos, valida reglas y guarda la venta.
     * @param request Modelo con los datos de la compra múltiple.
     * @return Modelo de la venta realizada con sus productos.
     */
    @Transactional
    public SaleModel buyMultipleProducts(ProductPurchaseModel request) {
        logger.info("Procesando compra múltiple para usuario ID: {}", request.getUserId());
        try {
            validatePurchaseItems(request);
            List<SaleItem> saleItems = new ArrayList<>();
            User user = getValidatedUser(request.getUserId());
            for (ProductPurchaseItemModel item : request.getItems()) {
                Product product = validateProductExists(item.getProductId());
                validateProductActive(product);
                ProductInventoryModel inventory = validateInventoryExists(item.getProductId());
                validateStockSufficient(inventory, item.getQuantity(), item.getProductId());
                // Descontar inventario
                inventory.setQuantity(inventory.getQuantity() - item.getQuantity());
                inventory.setUpdateDate(DateUtil.now());
                productInventoryService.saveInventory(inventory, product);
                // Crear SaleItem
                SaleItem saleItem = new SaleItem();
                saleItem.setProduct(product);
                saleItem.setQuantity(item.getQuantity());
                saleItem.setUnitPrice(product.getPrice());
                saleItems.add(saleItem);
            }
            if (!saleItems.isEmpty() && Objects.nonNull(user)) {
                Sale sale = new Sale();
                sale.setUser(user);
                sale.setDate(DateUtil.now());
                BigDecimal total = calculateTotalSale(saleItems, sale);
                sale.setTotalPrice(total);
                sale.setItems(saleItems);
                saleRepository.save(sale);
                return SaleConverter.toModel(sale);
            } else {
                logger.error("No hay productos para comprar en la transacción múltiple. Usuario ID: {}", request.getUserId());
                throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_PURCHASE_EMPTY);
            }
        } catch (ProductException ex) {
            logger.error("Error en la compra múltiple: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("Error inesperado en la compra múltiple. Usuario ID: {}", request.getUserId(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
        }
    }

    /**
     * Calcula el total de la venta sumando el precio por cantidad de cada producto vendido.
     * @param saleItems Lista de productos vendidos.
     * @param sale Venta asociada.
     * @return Total de la venta como BigDecimal.
     */
    private BigDecimal calculateTotalSale(List<SaleItem> saleItems, Sale sale) {
        BigDecimal total = BigDecimal.ZERO;
        for (SaleItem si : saleItems) {
            si.setSale(sale);
            total = total.add(si.getUnitPrice().multiply(BigDecimal.valueOf(si.getQuantity())));
        }
        return total;
    }

    /**
     * Valida que el producto exista por su ID.
     * @param productId ID del producto.
     * @return Entidad Product si existe.
     * @throws ProductException si no existe el producto.
     */
    private Product validateProductExists(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND, "Producto " + productId + " no existe. La compra se cancela."));
    }

    /**
     * Valida que el producto esté activo.
     * @param product Entidad Product a validar.
     * @throws ProductException si el producto está inactivo.
     */
    private void validateProductActive(Product product) {
        if (!product.getActive()) {
            throw new ProductException(ErrorCode.PRODUCT_INACTIVE, "Producto " + product.getId() + " inactivo.");
        }
    }

    /**
     * Valida que exista inventario para el producto.
     * @param productId ID del producto.
     * @return Modelo de inventario si existe.
     * @throws ProductException si no existe inventario.
     */
    private ProductInventoryModel validateInventoryExists(Long productId) {
        return productInventoryService.findById(productId)
                .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NO_INVENTORY, "Producto " + productId + " sin inventario."));
    }

    /**
     * Valida que el inventario tenga suficiente stock para la compra.
     * @param inventory Modelo de inventario.
     * @param quantity Cantidad solicitada.
     * @param productId ID del producto.
     * @throws ProductException si no hay suficiente stock.
     */
    private void validateStockSufficient(ProductInventoryModel inventory, int quantity, Long productId) {
        if (inventory.getQuantity() < quantity) {
            throw new ProductException(ErrorCode.PRODUCT_NO_STOCK, "Producto " + productId + " sin stock suficiente.");
        }
    }

    /**
     * Obtiene y valida el usuario por su ID.
     * @param userId ID del usuario.
     * @return Entidad User si existe.
     * @throws ProductException si no existe el usuario.
     */
    private User getValidatedUser(Long userId) {
        if (Objects.isNull(userId)) {
            throw new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND);
        }
        Optional<UserModel> userOpt = userService.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND);
        }
        UserModel userModel = userOpt.get();
        return UserConverter.modelToEntity(userModel);
    }

    /**
     * Valida que la compra múltiple tenga al menos un producto y no exceda el límite.
     * @param request Modelo de compra múltiple.
     * @throws ProductException si la validación falla.
     */
    private void validatePurchaseItems(ProductPurchaseModel request) {
        if (Objects.isNull(request.getItems()) || request.getItems().isEmpty()) {
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_PURCHASE_EMPTY);
        }
        if (request.getItems().size() > Constantes.MAX_PRODUCTOS_POR_COMPRA) {
            throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_PURCHASE_LIMIT);
        }
    }

    /**
     * Elimina un producto y su inventario por ID.
     * @param id ID del producto a eliminar.
     */
    @Transactional
    public void deleteProduct(Long id) {
        logger.info("Eliminando producto y su inventario ID: {}", id);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            // Eliminar inventario asociado si existe
            productInventoryService.deleteByProductId(product.getId());
            // Eliminar el producto
            productRepository.delete(product);
        } else {
            logger.error("Producto no encontrado para eliminar. ID: {}", id);
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
        logger.info("Actualizando estado activo de producto ID: {} a {}", id, active);
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setActive(active);
            product.setUpdateDate(DateUtil.now());
            Product updated = productRepository.save(product);
            return ProductConverter.toModel(updated);
        }
        logger.error("Producto no encontrado para actualizar estado activo. ID: {}", id);
        throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_NOT_FOUND);
    }

}
