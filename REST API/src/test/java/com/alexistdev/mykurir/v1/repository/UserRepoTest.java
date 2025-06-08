package com.alexistdev.mykurir.v1.repository;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepoTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("testuser@gmail.com");
        testUser.setPassword("password");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void testFindByRoleNot() {
        User user1 = new User();
        user1.setFullName("Alexsander Hendra Wijaya");
        user1.setPassword("123456");
        user1.setEmail("alexistdev@gmail.com");
        user1.setRole(Role.USER);
        entityManager.persist(user1);

        User user2 = new User();
        user2.setFullName("Veronica Maya Santi");
        user2.setPassword("password");
        user2.setEmail("veronicamayasanti@gmail.com");
        user2.setRole(Role.COURIER);
        entityManager.persist(user2);

        User user3 = new User();
        user3.setFullName("John Doe");
        user3.setPassword("pass2025");
        user3.setEmail("johndoe@gmail.com");
        user3.setRole(Role.ADMIN);
        entityManager.persist(user3);

        Pageable pageable = Pageable.ofSize(10);
        Page<User> result = userRepo.findByRoleNot(Role.ADMIN, pageable);

        assertEquals(2, result.getTotalElements());
        assertTrue(result.stream().anyMatch(user -> user.getRole() == Role.USER));
        assertTrue(result.stream().anyMatch(user -> user.getRole() == Role.COURIER));
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setFullName("Alexsander Hendra Wijaya");
        user.setPassword("123456");
        user.setEmail("alexistdev@gmail.com");
        user.setRole(Role.USER);

        User savedUser = userRepo.save(user);
        assertNotNull(savedUser);
        assertEquals("Alexsander Hendra Wijaya",savedUser.getFullName());
        assertEquals("123456",savedUser.getPassword());
        assertEquals("alexistdev@gmail.com",savedUser.getEmail());
        assertEquals(Role.USER,savedUser.getRole());
    }

    @Test
    void testFindUserById() {
        User user = new User();
        user.setFullName("Alexsander Hendra Wijaya");
        user.setPassword("123456");
        user.setEmail("alexistdev@gmail.com");
        user.setRole(Role.USER);
        entityManager.persist(user);

        Optional<User> foundUser = userRepo.findById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("Alexsander Hendra Wijaya",foundUser.get().getFullName());
        assertEquals("123456",foundUser.get().getPassword());
        assertEquals("alexistdev@gmail.com",foundUser.get().getEmail());
        assertEquals(Role.USER,foundUser.get().getRole());
    }

    @Test
    void testFindAllUser() {
        User user1 = new User();
        user1.setFullName("Alexsander Hendra Wijaya");
        user1.setPassword("123456");
        user1.setEmail("alexistdev@gmail.com");
        user1.setRole(Role.USER);
        entityManager.persist(user1);

        User user2 = new User();
        user2.setFullName("Veronica Maya Santi");
        user2.setPassword("password");
        user2.setEmail("veronicamayasanti@gmail.com");
        user2.setRole(Role.USER);
        entityManager.persist(user2);

        List<User> allUsers = userRepo.findAll();
        assertEquals(2,allUsers.size());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setFullName("Alexsander Hendra Wijaya");
        user.setPassword("123456");
        user.setEmail("alexistdev@gmail.com");
        user.setRole(Role.USER);
        entityManager.persist(user);

        userRepo.delete(user);
        Optional<User> deletedUser = userRepo.findById(user.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setFullName("Alexsander Hendra Wijaya");
        user.setPassword("123456");
        user.setEmail("alexistdev@gmail.com");
        user.setRole(Role.USER);
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> foundUser = userRepo.findByEmail("alexistdev@gmail.com");
        assertTrue(foundUser.isPresent());
        User retrievedUser = foundUser.get();

        assertEquals("alexistdev@gmail.com",retrievedUser.getEmail());
        assertEquals("Alexsander Hendra Wijaya",retrievedUser.getFullName());
        assertEquals("123456",retrievedUser.getPassword());
        assertEquals(Role.USER,retrievedUser.getRole());
    }

    @Test
    void testFindEmailNotExist() {
        Optional<User> foundUser = userRepo.findByEmail("nonexistent@gmail.com");
        assertFalse(foundUser.isPresent());
    }
}
