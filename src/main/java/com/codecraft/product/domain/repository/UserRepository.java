package com.codecraft.product.domain.repository;

import com.codecraft.product.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de usuarios.
 * Extiende JpaRepository para operaciones CRUD y permite agregar métodos personalizados.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    List<User> findByFirstNameContainingOrLastNameContainingOrMiddleNameContaining(String firstName, String lastName, String middleName);
}
