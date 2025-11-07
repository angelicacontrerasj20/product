package com.codecraft.product.domain.repository;

import com.codecraft.product.domain.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la gestión de ventas.
 * Extiende JpaRepository para operaciones CRUD y permite agregar métodos personalizados.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
}
