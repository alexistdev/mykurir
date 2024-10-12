package com.alexistdev.mykurir.v1.repository;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import com.alexistdev.mykurir.v1.models.repository.DistrictRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DisctrictRepoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DistrictRepo districtRepo;

    private Regency regency;

    @BeforeEach
    void setUp() {
        User testUser = new User();
        testUser.setEmail("testuser@gmail.com");
        testUser.setPassword("password");
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(testUser, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Province province = new Province();
        province.setName("Lampung");
        province = entityManager.persist(province);

        regency = new Regency();
        regency.setName("Bandar Lampung");
        regency.setProvince(province);
        regency = entityManager.persist(regency);

        entityManager.flush();
    }

    @Test
    void testSaveDistrict() {
        District district = new District();
        district.setName("Pringsewu");
        district.setRegency(regency);

        District districtSave = districtRepo.save(district);
        assertNotNull(districtSave);
        assertEquals("Pringsewu",districtSave.getName());
        assertNotNull(districtSave.getId());

    }

    @Test
    void testFindDistrictById() {
        District district = new District();
        district.setName("Pringsewu");
        district.setRegency(regency);
        entityManager.persist(district);

        Optional<District> foundDistrict = districtRepo.findById(district.getId());
        assertTrue(foundDistrict.isPresent());
        assertEquals(district.getName(), foundDistrict.get().getName());
        assertEquals(district.getId(),foundDistrict.get().getId());

    }

    @Test
    void testFindAllDistricts() {
        District district1 = new District();
        district1.setName("Pringsewu");
        district1.setRegency(regency);
        entityManager.persist(district1);
        District district2 = new District();
        district2.setName("Metro");
        district2.setRegency(regency);
        entityManager.persist(district2);

        List<District> allDistricts = districtRepo.findAll();
        assertEquals(2,allDistricts.size());
    }

    @Test
    void testDeleteDistricts() {
        District district = new District();
        district.setName("Pringsewu");
        district.setRegency(regency);
        entityManager.persist(district);

        districtRepo.deleteById(district.getId());
        Optional<District> foundDistrict = districtRepo.findById(district.getId());
        assertFalse(foundDistrict.isPresent());
    }

    @Test
    void testFindByName() {
        District district = new District();
        district.setName("Pringsewu");
        district.setRegency(regency);
        entityManager.persist(district);

        District foundDistrict = districtRepo.findByName("Pringsewu");
        assertNotNull(foundDistrict);
        assertEquals("Pringsewu", foundDistrict.getName());
    }

    @Test
    void testFindByNameNotFound() {
        District foundDistrict = districtRepo.findByName("Non-existent district");
        assertNull(foundDistrict);
    }
}
