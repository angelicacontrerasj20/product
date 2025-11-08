package com.codecraft.product.service;

import com.codecraft.product.converter.*;
import com.codecraft.product.domain.entity.User;
import com.codecraft.product.domain.repository.UserProcedureRepository;
import com.codecraft.product.domain.repository.UserRepository;
import com.codecraft.product.exception.ErrorCode;
import com.codecraft.product.exception.ResourceNotFoundGlobalException;
import com.codecraft.product.mapper.UserModelMapper;
import com.codecraft.product.util.DateUtil;
import com.codecraft.product.util.JwtUtil;
import com.codecraft.product.util.PasswordUtil;
import com.codecraft.product.web.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios.
 *
 * @author Angelica Contreras Jeronimo
 * @date 2025-11-07
 */
@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserProcedureRepository userProcedureRepository;

    public UserService() {
        logger.info("UserService inicializado correctamente - prueba de log");
    }

    /**
     * Agrega un nuevo usuario al sistema.
     * @param userModel Modelo con los datos del usuario.
     * @return Modelo del usuario agregado.
     */
    public UserModel addUser(UserModel userModel) {
        logger.info("Alta de usuario por procedimiento almacenado: {}", userModel.getUserName());
        Date dateOfBirth = userModel.getDateOfBirth() != null ? Date.valueOf(userModel.getDateOfBirth()) : Date.valueOf("1900-01-01");
        Timestamp registerDate = userModel.getRegisterDate() != null ? DateUtil.toTimestamp(userModel.getRegisterDate()) : DateUtil.toTimestamp(DateUtil.now());
        Timestamp updateDate = userModel.getUpdateDate() != null ? DateUtil.toTimestamp(userModel.getUpdateDate()) : DateUtil.toTimestamp(DateUtil.now());

            Map<String, Object> inParams = Map.of(
                "p_nombreUsuario", userModel.getUserName(),
                "p_contrasenia", PasswordUtil.encrypt(userModel.getPassword()),
                "p_primerNombre", userModel.getFirstName(),
                "p_apellidoPaterno", userModel.getLastName(),
                "p_apellidoMaterno", userModel.getMiddleName(),
                "p_fechaNacimiento", dateOfBirth,
                "p_genero", userModel.getGender(),
                "p_estadoNacimiento", userModel.getStateOfBirth(),
                "p_fechaRegistro", registerDate,
                "p_fechaActualizacion", updateDate
            );

        Map<String, Object> result = userProcedureRepository.altaUsuario(inParams);
        UserModel user = ProcedureResultAddConverter.convert(result, "usuario_id", UserModelMapper::map);
        return user;
    }

    /**
     * Actualiza los datos de un usuario existente.
     * @param userUpdateModel Modelo con los datos a actualizar.
     * @return Modelo actualizado del usuario.
     */
    public UserModel updateUser(UserUpdateModel userUpdateModel) {
        logger.info("Actualizando usuario ID: {}", userUpdateModel.getId());
        Optional<User> userOpt = userRepository.findById(userUpdateModel.getId());
        if (userOpt.isPresent()) {
            User originalUpdate = userOpt.get();
            UserUpdateConverter.updateEntity(userUpdateModel, originalUpdate);
            User updated = userRepository.save(originalUpdate);
            return userConverter.entityToModel(updated);
        }
        throw new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND);
    }

    /**
     * Actualiza la contraseña de un usuario por su ID.
     * @param id ID del usuario.
     * @param userUpdatePasswordModel Modelo con la contraseña actual y la nueva contraseña.
     * @return Modelo actualizado del usuario.
     */
    public UserModel updatePassword(Long id, UserUpdatePasswordModel userUpdatePasswordModel) {
        logger.info("Actualizando contraseña de usuario ID: {}", id);
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Validar que la contraseña actual coincida
            String currentDecrypted = PasswordUtil.decrypt(user.getPassword());
            if (!currentDecrypted.equals(userUpdatePasswordModel.getCurrentPassword())) {
                throw new ResourceNotFoundGlobalException(ErrorCode.USER_PASSWORD_INVALID);
            }
            user.setPassword(PasswordUtil.encrypt(userUpdatePasswordModel.getNewPassword()));
            user.setUpdateDate(DateUtil.now());
            User updated = userRepository.save(user);
            return userConverter.entityToModel(updated);
        }
        throw new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND);
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario.
     * @return Optional con el modelo del usuario si existe.
     */
    public Optional<UserModel> findById(Long id) {
        logger.info("Buscando usuario por ID: {}", id);
        return userRepository.findById(id).map(userConverter::entityToModel);
    }

    /**
     * Lista todos los usuarios existentes.
     * @return Lista de modelos de usuario.
     */
    public List<UserModel> listUsers() {
        logger.info("Listando todos los usuarios");
        return userRepository.findAll().stream()
            .map(userConverter::entityToModel)
            .toList();
    }

    /**
     * Busca un usuario por su nombre de usuario, sin exponer la contraseña.
     * @param userName Nombre de usuario.
     * @return Modelo del usuario si existe, sin contraseña.
     */
    public UserGetModel findByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND));
        return UserGetConverter.entityToModel(user);
    }

    /**
     * Autentica usuario por nombre y contraseña, permite hasta 3 intentos fallidos.
     * @param loginRequestModel Nombre de usuario y password.
     * @return Token JWT si la autenticación es exitosa.
     */
    public String authenticateUser(LoginRequestModel loginRequestModel) {
        try {
            User user = userRepository.findByUserName(loginRequestModel.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundGlobalException(ErrorCode.USER_NOT_FOUND));
            if (PasswordUtil.decrypt(user.getPassword()).equals(loginRequestModel.getPassword())) {
                logger.info("Login exitoso para usuario: {}", loginRequestModel.getUsername());
                return JwtUtil.generateToken(loginRequestModel.getUsername());
            } else {
                logger.warn("Login fallido para usuario: {} - contraseña incorrecta", loginRequestModel.getUsername());
                throw new ResourceNotFoundGlobalException(ErrorCode.USER_PASSWORD_INVALID);
            }
        }
        catch (Exception ex) {
            logger.error("Error inesperado en login: {}", ex.getMessage(), ex);
            throw new ResourceNotFoundGlobalException(ErrorCode.USER_PASSWORD_INVALID);
        }
    }

    /**
     * Busca usuarios por cualquier coincidencia parcial en nombre, apellido paterno o materno.
     * Si searchText contiene espacios, busca cada palabra en los tres campos y une los resultados.
     * @param searchText Texto de búsqueda parcial (puede contener varias palabras).
     * @return Lista de modelos de usuario que coinciden parcialmente en cualquier campo.
     */
    public List<UserModel> findByNameAndSurnameLike(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return List.of();
        }
        String[] palabras = searchText.trim().split("\\s+");
        // Usar un Set para evitar duplicados
        java.util.Set<User> usuarios = new java.util.HashSet<>();
        for (String palabra : palabras) {
            usuarios.addAll(userRepository.findByFirstNameContainingOrLastNameContainingOrMiddleNameContaining(palabra, palabra, palabra));
        }
        return usuarios.stream().map(userConverter::entityToModel).toList();
    }
}
