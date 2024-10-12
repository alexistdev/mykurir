package com.alexistdev.mykurir.v1.repository;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
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
public class RegencyRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RegencyRepo regencyRepo;

    private Province province;

    @BeforeEach
    void setUp() {

        User testUser = new User();
        testUser.setEmail("testuser@gmail.com");
        testUser.setPassword("password");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        province = new Province();
        province.setName("Lampung");
        entityManager.persist(province);
    }

    @Test
    void testSaveRegency() {
        Regency regency = new Regency();
        regency.setName("Bandar Lampung");
        regency.setProvince(province);

        Regency savedRegency = regencyRepo.save(regency);

        Assertions.assertNotNull(savedRegency.getId());
        Assertions.assertEquals("Bandar Lampung", savedRegency.getName());
        Assertions.assertEquals("Lampung", savedRegency.getProvince().getName());
    }

    @Test
    void testFindRegencyById() {
        Regency regency = new Regency();
         regency.setName("Bandar Lampung");
         regency.setProvince(province);
         entityManager.persist(regency);

         Optional<Regency> foundRegency = regencyRepo.findById(regency.getId());

         Assertions.assertTrue(foundRegency.isPresent());
         Assertions.assertEquals("Bandar Lampung", foundRegency.get().getName());
    }

    @Test
    void testFindAllRegencies() {
        Regency regency1 = new Regency();
        regency1.setName("Bandar Lampung");
        regency1.setProvince(province);
        Regency regency2 = new Regency();
        regency2.setName("Metro");
        regency2.setProvince(province);
        entityManager.persist(regency1);
        entityManager.persist(regency2);

        List<Regency> allRegencies = regencyRepo.findAll();
        Assertions.assertEquals(2,allRegencies.size());
    }

    @Test
    void testDeleteRegency() {
        Regency regency = new Regency();
        regency.setName("Bandar Lampung");
        regency.setProvince(province);
        entityManager.persist(regency);

        regencyRepo.deleteById(regency.getId());
        Optional<Regency> deletedRegency = regencyRepo.findById(regency.getId());
        Assertions.assertFalse(deletedRegency.isPresent());
    }

    @Test
    void testFindByName(){
        Regency regency = new Regency();
        regency.setName("Bandar Lampung");
        regency.setProvince(province);
        entityManager.persist(regency);

        Regency foundRegency = regencyRepo.findByName("Bandar Lampung");
        Assertions.assertNotNull(foundRegency);
        Assertions.assertEquals("Bandar Lampung", foundRegency.getName());
    }

    @Test
    void testFindByNameNotFound(){
        Regency foundRegency = regencyRepo.findByName("Non-existent regency");
        Assertions.assertNull(foundRegency);
    }
}
