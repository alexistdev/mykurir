package com.alexistdev.mykurir.v1.entity;

import com.alexistdev.mykurir.v1.models.entity.Role;
import com.alexistdev.mykurir.v1.models.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("Alexsander Hendra Wijaya");
        user.setEmail("alexistdev@gmail.com");
        user.setPassword("password");
        user.setRole(Role.USER);
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a->a.getAuthority().equals("USER")));
    }

    @Test
    void testGetUsername() {
        assertEquals("alexistdev@gmail.com", user.getUsername());
    }

    @Test
    void testAccountNonExpired() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void testAccountNonLocked() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void testCredentialsNon() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void testEnabled() {
        assertTrue(user.isEnabled());
    }

    @Test
    void testGettersAndSetters() {
        assertEquals(1L,user.getId());
        assertEquals("Alexsander Hendra Wijaya", user.getFullName());
        assertEquals("alexistdev@gmail.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertEquals(Role.USER, user.getRole());

        user.setId(2L);
        user.setFullName("Veronica Maya Santi");
        user.setEmail("veronicamayasanti@gmail.com");
        user.setPassword("12345");
        user.setRole(Role.ADMIN);

        assertEquals(2L,user.getId());
        assertEquals("Veronica Maya Santi", user.getFullName());
        assertEquals("veronicamayasanti@gmail.com", user.getEmail());
        assertEquals("12345", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
    }
}
