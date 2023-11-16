package com.proyecto.integrador.service;

import com.proyecto.integrador.dto.UserDto;
import com.proyecto.integrador.entity.User;
import com.proyecto.integrador.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findByEmail() {
        String email = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        User user = userService.findByEmail(email);

        assertNotNull(user);
        assertEquals(email, user.getEmail());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void findById() {
        Long id = 1L;
        User mockUser = new User();
        mockUser.setId(id);

        when(userRepository.findByIdAndDeletedFalse(id)).thenReturn(mockUser);

        User user = userService.findById(id);

        assertNotNull(user);
        assertEquals(id, user.getId());

        verify(userRepository, times(1)).findByIdAndDeletedFalse(id);
    }

    @Test
    void findAll() {
        List<User> mockUsers = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        mockUsers.add(user1);

        User user2 = new User();
        user2.setId(2L);
        mockUsers.add(user2);

        when(userRepository.findAllByDeletedFalse()).thenReturn(mockUsers);

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(1L, users.get(0).getId());
        assertEquals(2L, users.get(1).getId());

        verify(userRepository, times(1)).findAllByDeletedFalse();
    }

    /*
    @Test
    void findAllAdminUsers() {
        List<User> mockAdminUsers = new ArrayList<>();
        User adminUser1 = new User();
        adminUser1.setId(1L);
        adminUser1.setIsAdmin(true);
        mockAdminUsers.add(adminUser1);

        User adminUser2 = new User();
        adminUser2.setId(2L);
        adminUser2.setIsAdmin(true);
        mockAdminUsers.add(adminUser2);

        when(userRepository.findAllAdminUsersByDeletedFalse()).thenReturn(mockAdminUsers);

        List<User> adminUsers = userService.findAllAdminUsers();

        assertNotNull(adminUsers);
        assertEquals(2, adminUsers.size());
        assertTrue(adminUsers.get(0).getIsAdmin());
        assertTrue(adminUsers.get(1).getIsAdmin());

        verify(userRepository, times(1)).findAllAdminUsersByDeletedFalse();
    }*/

    /*@Test
    void findAllNormalUsers() {
        List<User> mockNormalUsers = new ArrayList<>();
        User normalUser1 = new User();
        normalUser1.setId(1L);
        normalUser1.setIsAdmin(false);
        mockNormalUsers.add(normalUser1);

        User normalUser2 = new User();
        normalUser2.setId(2L);
        normalUser2.setIsAdmin(false);
        mockNormalUsers.add(normalUser2);

        when(userRepository.findAllNormalUsersByDeletedFalse()).thenReturn(mockNormalUsers);

        List<User> normalUsers = userService.findAllNormalUsers();

        assertNotNull(normalUsers);
        assertEquals(2, normalUsers.size());
        assertFalse(normalUsers.get(0).getIsAdmin());
        assertFalse(normalUsers.get(1).getIsAdmin());

        verify(userRepository, times(1)).findAllNormalUsersByDeletedFalse();
    }*/

    @Test
    void deleteUserById() {
        Long id = 1L;
        User mockUser = new User();
        mockUser.setId(id);
        mockUser.setDeleted(false);

        when(userRepository.findByIdAndDeletedFalse(id)).thenReturn(mockUser);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User deletedUser = userService.deleteUserById(id);

        assertNotNull(deletedUser);
        assertTrue(deletedUser.getDeleted());

        verify(userRepository, times(1)).findByIdAndDeletedFalse(id);
        verify(userRepository, times(1)).save(any(User.class));
    }

    /*
    @Test
    void updateUserById() throws Exception {
        Long id = 1L;
        User mockUser = new User();
        mockUser.setId(id);
        mockUser.setName("TestName");
        mockUser.setSurname("TestSurname");
        mockUser.setIsAdmin(false);
        mockUser.setAreaCode(2451);
        mockUser.setPrefix(5124);
        mockUser.setPhone(15151515);
        mockUser.setIsMobile(false);
        mockUser.setEmail("testEmail@gmail.com");
        mockUser.setPassword("123456");

        UserDto userDto = new UserDto();
        userDto.setName("New Name");


        when(userRepository.findById(id)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User updatedUser = userService.updateUserById(id, userDto);

        assertNotNull(updatedUser);
        assertEquals(userDto.getName(), updatedUser.getName());


        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(any(User.class));
    }*/

    /*
    @Test
    void createUser() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");


        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userDto.getEmail(), createdUser.getEmail());


        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }*/

    @Test
    void login() {
        String email = "test@example.com";
        String password = "password";
        User mockUser = new User();
        mockUser.setEmail(email);
        mockUser.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(mockUser);

        User user = userService.login(email, password);

        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }

    /*
    @Test
    void register() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("test@example.com");
        // Set other fields on userDto as needed

        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(null);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User registeredUser = userService.register(userDto);

        assertNotNull(registeredUser);
        assertEquals(userDto.getEmail(), registeredUser.getEmail());


        verify(userRepository, times(1)).findByEmail(userDto.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }*/
}