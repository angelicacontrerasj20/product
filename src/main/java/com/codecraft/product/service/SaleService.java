package com.codecraft.product.service;

import com.codecraft.product.converter.SaleConverter;
import com.codecraft.product.converter.SaleGetModelConverter;
import com.codecraft.product.converter.UserConverter;
import com.codecraft.product.domain.entity.Product;
import com.codecraft.product.domain.entity.Sale;
import com.codecraft.product.domain.entity.SaleItem;
import com.codecraft.product.domain.entity.User;
import com.codecraft.product.domain.repository.SaleRepository;
import com.codecraft.product.exception.ErrorCode;
import com.codecraft.product.exception.ProductException;
import com.codecraft.product.exception.ResourceNotFoundGlobalException;
import com.codecraft.product.util.Constantes;
import com.codecraft.product.util.DateUtil;
import com.codecraft.product.util.TraceIdUtil;
import com.codecraft.product.web.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {
    private static final Logger logger = LogManager.getLogger(SaleService.class);
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductInventoryService productInventoryService;

    public List<SaleGetModel> getSalesByUserId(Long userId) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("SaleService.getSalesByUserId " + traceId + " " + userId);
        List<Sale> sales = saleRepository.findByUser_Id(userId);
        return sales.stream().map(SaleGetModelConverter::toModel).collect(Collectors.toList());
    }

    /**
     * Realiza la compra múltiple de productos, valida reglas y guarda la venta.
     * @param request Modelo con los datos de la compra múltiple.
     * @return Modelo de la venta realizada con sus productos.
     */
    @Transactional
    public SaleModel buyMultipleProducts(ProductPurchaseModel request) {
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("SaleService.buyMultipleProducts " + traceId + " " + request.getUserId());
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
                logger.error("SaleService.buyMultipleProducts " + traceId + " No hay productos para comprar en la transacción múltiple. Usuario ID: {}", request.getUserId());
                throw new ResourceNotFoundGlobalException(ErrorCode.PRODUCT_PURCHASE_EMPTY);
            }
        } catch (ProductException ex) {
            logger.error("SaleService.buyMultipleProducts " + traceId + " Error en la compra múltiple: {}", ex.getMessage(), ex);
            throw ex;
        } catch (Exception ex) {
            logger.error("SaleService.buyMultipleProducts " + traceId + " Error inesperado en la compra múltiple. Usuario ID: {}", request.getUserId(), ex);
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
        String traceId = TraceIdUtil.getOrCreateTraceId();
        logger.info("SaleService.validateProductExists " + traceId + " " + productId);
        return productService.findProductModelById(productId);
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

}
