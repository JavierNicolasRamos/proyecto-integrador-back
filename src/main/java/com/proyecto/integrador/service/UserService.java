package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.enums.Role;
import com.proyecto.integrador.exception.*;
import com.proyecto.integrador.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email: " + e.getMessage());
            throw new UserFindByEmailException("Error inesperado al buscar al usuario con email", e);
        }
    }

    public User findById(Long id) {
        try {

            User user = userRepository.optionalFindByIdAndDeletedFalse(id).orElseThrow(()
                    -> new UserNotFoundException());
            return user;
        }catch(UserNotFoundException e){
            logger.severe("No se encontró al usuario con ID:" + id);
            throw  new UserNotFoundException("No se encontro el usuario con el ID" + id);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por id: " + e.getMessage());
            throw new UserFindByIdException("Error inesperado al intentar recuperar al usuario con id: " + id, e);
        }
    }

    public List<User> findAll() {
        try {
            return userRepository.findAllByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios: " + e.getMessage());
            throw  new UserFindAllException("Error inesperado al recuperar a todos los usuarios", e);
        }
    }

    public List<User> findAllAdminUsers() {
        try {
            return userRepository.findAllAdminUsersByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios administradores: " + e.getMessage());
            throw new UserFindAllAdminUsersException("Error inesperado al recuperar todos los usuarios administradores", e);
        }
    }

    public List<User> findAllNormalUsers() {
        try {
            return userRepository.findAllNormalUsersByDeletedFalse();
        } catch (Exception e) {
            logger.severe("Error al buscar todos los usuarios normales: " + e.getMessage());
            throw new UserFindAllNormalUsersException("Error inesperado al recuperar a todos los usuarios", e);
        }
    }

    @Transactional
    public User deleteUserById(Long id) {
        try {
            User user = userRepository.optionalFindByIdAndDeletedFalse(id).orElseThrow(()
                    -> new UserNotFoundException("El ususario con ID:" + id + "no pudo ser encontrado"));
            user.setDeleted(true);
            return userRepository.save(user);
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.severe("Error al eliminar el usuario por id: " + e.getMessage());
            throw new DeleteUserByIdException("Error inesperado al intentar eliminar el usuario con id:" + id);
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

                logger.info("Usuario con ID " + id + " actualizado con éxito.");
                return userRepository.save(user);
            } else {
                logger.warning("No se encontró el usuario con ID " + id + ".");
                throw new UserNotFoundException("No se encontró el usuario con ID " + id + ".");
            }
        } catch (UserNotFoundException e){
            throw new UserNotFoundException(e.getMessage());
        } catch (Exception e) {
            logger.severe("Error inesperado al actualizar el usuario con ID " + id + ": " + e.getMessage());
            throw new UpdateUserByIdException("Error inesperado al actualizar el usuario con ID " + id + ": " + e);
        }
    }

    public User findUsersByRole(String role) {
        try {
            return userRepository.findByRole(role);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por role: " + e.getMessage());
            throw new FindUsersByRoleException("Error inesperado al buscar los usuarios con rol:" + role, e);
        }
    }

    public Role getRoleByEmail(String email) throws Exception {
        try {
            return userRepository.getRoleByEmail(email);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email: " + e.getMessage());
            throw new UserGetRoleByEmailException("Error al buscar el usuario por email: " + e.getMessage());
        }
    }

    @Transactional
    public void register(@NotNull UserDto userDto) throws Exception {
        logger.info("Iniciando el registro del usuario...");
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(userDto.getEmail()));

        try {
            if (userOptional.isPresent()) {
                logger.warning("Ya existe un usuario con el mismo email: " + userDto.getEmail());
                throw new Exception("Ya existe un usuario con el mismo email: " + userDto.getEmail());
            } else {
                User user = getUser(userDto);

                userRepository.save(user);
                logger.info("Usuario creado con éxito.");

                emailService.sendEmail(user.getEmail(), "Registro usuario", emailService.createRegisterHtml(user.getName(), user.getSurname()));
                logger.info("Correo enviado con éxito.");
            }
        } catch (Exception e) {
            logger.severe("Error inesperado al crear el usuario: " + e.getMessage());
            throw new UserRegisterException("Error inesperado al crear el usuario: " + e.getMessage());
        }
    }

    private User getUser(@NotNull UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setAreaCode(userDto.getAreaCode());
        user.setPrefix(userDto.getPrefix());
        user.setPhone(userDto.getPhone());
        user.setIsMobile(userDto.getIsMobile());
        user.setEmail(userDto.getEmail());
        Role role = Role.valueOf(userDto.getRole().toUpperCase());
        user.setUserRole(role);
        String encryptedPassword = this.passwordEncoder.encode(userDto.getPassword());
        user.setPassword(encryptedPassword);
        user.setIsActive(true);
        user.setDeleted(false);
        return user;
    }

    public void resendRegisterEmail(@NotNull String email) {
        try {
            User user = userRepository.findByEmail(email);
            emailService.sendEmail(email, "Registro usuario", emailService.createRegisterHtml(user.getName(), user.getSurname()));
        } catch (Exception e) {
            logger.severe("Error al reenviar el email de registro: " + e.getMessage());
            throw new UserRegisterByEmailException("Error al reenviar el email de registro: ", e);
        }
    }

    public String getNameByEmail(String email) {
        try {
            return userRepository.getNameByEmail(email);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email: " + e.getMessage());
            throw new UserGetNameByEmailException("Error inesperado al internar obtener el nombre del usuario con email:" + email);
        }
    }

    public String getLastNameByEmail(String email) {
        try {
            return userRepository.getLastNameByEmail(email);
        } catch (Exception e) {
            logger.severe("Error al buscar el usuario por email: " + e.getMessage());
            throw new UserGetLastNameByEmailException("Error al buscar el usuario por email: " + email, e);
        }
    }

    public void updateUserRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userId));
        try {
            if (user.getUserRole().equals(Role.USER)) {
                user.setUserRole(Role.ADMIN);
            } else if (user.getUserRole().equals(Role.ADMIN)) {
                user.setUserRole(Role.USER);
            }
            this.userRepository.save(user);
        }
        catch (Exception e){
            logger.severe("Error al actualizar el usuario por email: " + e.getMessage());
            throw new UpdateUserRoleException("Error al actualizar el usuario por email: ", e);
        }
    }
}
