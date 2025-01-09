package com.alexistdev.mykurir.v1.repository;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles("test")
public class ProvinceRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProvinceRepo provinceRepo;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("testuser@gmail.com");
        testUser.setPassword("password");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    void testSaveProvince() {
        Province province = new Province();
        province.setName("Lampung");

        Province savedProvince = provinceRepo.save(province);

        Assertions.assertNotNull(savedProvince.getId());
        Assertions.assertEquals("Lampung", savedProvince.getName());
    }

    @Test
    void testFindProvinceById() {
        Province province = new Province();
        province.setName("Jakarta");
        entityManager.persist(province);

        Optional<Province> foundProvince = provinceRepo.findById(province.getId());

        Assertions.assertTrue(foundProvince.isPresent());
        Assertions.assertEquals("Jakarta", foundProvince.get().getName());
    }

    @Test
    void testFindAllProvinces() {
        Province province1 = new Province();
        province1.setName("Jakarta");
        Province province2 = new Province();
        province2.setName("Lampung");
        entityManager.persist(province1);
        entityManager.persist(province2);

        List<Province> provinces = provinceRepo.findAll();

        Assertions.assertEquals(2, provinces.size());
    }

    @Test
    void testDeleteProvince() {
        Province province = new Province();
        province.setName("Jakarta");
        entityManager.persist(province);

        provinceRepo.deleteById(province.getId());

        Optional<Province> foundProvince = provinceRepo.findById(province.getId());
        Assertions.assertFalse(foundProvince.isPresent());
    }

    @Test
    void testFindByName() {
        Province province = new Province();
        province.setName("Jakarta");
        entityManager.persistAndFlush(province);

        Province foundProvince = provinceRepo.findByName("Jakarta");
        Assertions.assertNotNull(foundProvince);
        Assertions.assertEquals("Jakarta", foundProvince.getName());
    }

    @Test
    void testFindByNameNotFound() {
        Province foundProvince = provinceRepo.findByName("Non-existent province");
        Assertions.assertNull(foundProvince);
    }
}
