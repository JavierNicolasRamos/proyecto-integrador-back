package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.exception.UsuarioNotFoundException;
import com.proyecto.integrador.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private UserRepository userRepository;

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email: " + e.getMessage());
            return null;
        }
    }

    public User findById(Long id) {
        try {
            return userRepository.findByIdAndDeletedFalse(id);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por id: " + e.getMessage());
            return null;
        }
    }

    public List<User> findAll() {
        try {
            return userRepository.findAllByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios: " + e.getMessage());
            return null;
        }
    }

    public List<User> findAllAdminUsers() {
        try {
            return userRepository.findAllAdminUsersByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios administradores: " + e.getMessage());
            return null;
        }
    }

    public List<User> findAllNormalUsers() {
        try {
            return userRepository.findAllNormalUsersByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios normales: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public User deleteUserById(Long id) {
        try {
            User user = userRepository.findByIdAndDeletedFalse(id);
            user.setDeleted(true);
            return userRepository.save(user);
        } catch (Exception e) {
            logger.severe("Error al eliminar el usuario por id: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    public User updateUserById(Long id, UserDto userDto) throws Exception {
        logger.info("Iniciando la actualización del usuario con ID " + id + "...");
        Optional<User> userOptional = userRepository.findById(id);

        try {
            if (userOptional.isPresent()) {
                User user = userOptional.get();

                if (!user.getName().equals(userDto.getName())) {
                    user.setName(userDto.getName());
                }

                if (!user.getSurname().equals(userDto.getSurname())) {
                    user.setSurname(userDto.getSurname());
                }

                if (!user.getIsAdmin().equals(userDto.getIsAdmin())) {
                    user.setIsAdmin(userDto.getIsAdmin());
                }

                if (!user.getAreaCode().equals(userDto.getAreaCode())) {
                    user.setAreaCode(userDto.getAreaCode());
                }

                if (!user.getPrefix().equals(userDto.getPrefix())) {
                    user.setPrefix(userDto.getPrefix());
                }

                if (!user.getPhone().equals(userDto.getPhone())) {
                    user.setPhone(userDto.getPhone());
                }

                if (!user.getIsMobile().equals(userDto.getIsMobile())) {
                    user.setIsMobile(userDto.getIsMobile());
                }

                if (!user.getEmail().equals(userDto.getEmail())) {
                    user.setEmail(userDto.getEmail());
                }

                if (!user.getPassword().equals(userDto.getPassword())) {
                    user.setPassword(userDto.getPassword());
                }

                logger.info("Usuario con ID " + id + " actualizado con éxito.");
                return userRepository.save(user);
            } else {
                logger.warning("No se encontró el usuario con ID " + id + ".");
                throw new Exception("No se encontró el usuario con ID " + id + ".");
            }
        } catch (Exception e) {
            logger.severe("Error inesperado al actualizar el usuario con ID " + id + ": " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        }
    }

    @Transactional
    public User createUser(@NotNull UserDto userDto) throws Exception {
        logger.info("Iniciando la creación del usuario...");
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));

        try {
            if (userOptional.isPresent()) {
                logger.warning("Ya existe un usuario con el mismo email: " + userDto.getEmail());
                throw new Exception("Ya existe un usuario con el mismo email: " + userDto.getEmail());
            } else {
                User user = getUser(userDto);

                logger.info("Usuario creado con éxito.");
                return userRepository.save(user);
            }
        } catch (Exception e) {
            logger.severe("Error inesperado al crear el usuario: " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        }
    }

    public User login(String email, String password) {
        try {
            User user = userRepository.findByEmail(email);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            } else {
                throw new UsuarioNotFoundException("Usuario inexistente");
            }
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email y password: " + e.getMessage());
            throw new UsuarioNotFoundException("Usuario inexistente");
        }
    }

    @Transactional
    public User register(@NotNull UserDto userDto) throws Exception {
        logger.info("Iniciando el registro del usuario...");
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));

        try {
            if (userOptional.isPresent()) {
                logger.warning("Ya existe un usuario con el mismo email: " + userDto.getEmail());
                throw new Exception("Ya existe un usuario con el mismo email: " + userDto.getEmail());
            } else {
                User user = getUser(userDto);

                logger.info("Usuario registrado con éxito.");
                return userRepository.save(user);
            }
        } catch (Exception e) {
            logger.severe("Error inesperado al registrar el usuario: " + e.getMessage());
            throw e; //TODO: sumar la excepcion customizada
        }
    }

    private static @NotNull User getUser(@NotNull UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setIsAdmin(userDto.getIsAdmin());
        user.setAreaCode(userDto.getAreaCode());
        user.setPrefix(userDto.getPrefix());
        user.setPhone(userDto.getPhone());
        user.setIsMobile(userDto.getIsMobile());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setIsActive(true);
        user.setDeleted(false);
        return user;
    }
}
