package com.codecraft.product.domain.repository;

import com.codecraft.product.domain.entity.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de inventario de productos.
 * Extiende JpaRepository para operaciones CRUD y permite agregar métodos personalizados.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@Repository
public interface ProductInventoryRepository extends JpaRepository<ProductInventory, Long> {
    /**
     * Elimina el inventario de un producto por su ID.
     *
     * @param productId el ID del producto cuyo inventario se va a eliminar
     */
    void deleteByProductId(Long productId);
}
