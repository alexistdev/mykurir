package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.UserRepo;
import com.alexistdev.mykurir.v1.request.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        String email = "alexistdev@gmail.com";
        User user = new User();
        user.setEmail(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername(email);

        assertNotNull(userDetails);
//        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        String email = "non-existent@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(email));
    }

    @Test
    void registerUser_NewUser_ReturnsRegisteredUser() {
        User user = new User();
        user.setEmail("alexistdev@gmail.com");
        user.setPassword("password");
        when(userRepo.save(any(User.class))).thenReturn(user);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals(Role.USER, registeredUser.getRole());
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepo).save(user);
    }


    @Test
    void registerUser_ExistingUser_ThrowsException() {
        User user = new User();
        user.setEmail("alexistdev@gmail.com");
        when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> userService.registerUser(user));
    }

    @Test
    void authenticate_ValidCredentials_ReturnsUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alexistdev@gmail.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(true);

        User authenticatedUser = userService.authenticate(loginRequest);

        assertNotNull(authenticatedUser);
        assertEquals(user.getEmail(),authenticatedUser.getEmail());
    }

    @Test
    void authenticate_InvalidCredentials_ReturnsNull() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("alexistdev@gmail.com");
        loginRequest.setPassword("password");

        User user = new User();
        user.setEmail(loginRequest.getEmail());
        user.setPassword("encodedPassword");

        when(userRepo.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(bCryptPasswordEncoder.matches(loginRequest.getPassword(), user.getPassword())).thenReturn(false);

        User authenticatedUser = userService.authenticate(loginRequest);

        assertNull(authenticatedUser);
    }

    @Test
    void getAllUsers_ReturnsNonAdminUsers() {
        User user1 = new User();
        user1.setRole(Role.USER);
        User user2 = new User();
        user2.setRole(Role.COURIER);
        User user3 = new User();
        user3.setRole(Role.STAFF);
        User Admin = new User();
        Admin.setRole(Role.ADMIN);

        List<User> allUsers = List.of(user1,user2,user3,Admin);

        //Mock
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage =  new PageImpl<>(allUsers.stream()
                .filter(user -> user.getRole() != Role.ADMIN)
                .collect(Collectors.toList()));


        when(userRepo.findByRoleNot(Role.ADMIN,pageable)).thenReturn(userPage);

        Page<User> nonAdminUsers = userService.getAllUsers(pageable);

        assertEquals(3, nonAdminUsers.getContent().size());
        assertTrue(nonAdminUsers.getContent().stream()
                .noneMatch(user -> user.getRole() == Role.ADMIN));
    }
}
