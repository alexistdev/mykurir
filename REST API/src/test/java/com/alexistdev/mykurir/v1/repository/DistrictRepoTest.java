package com.alexistdev.mykurir.v1.repository;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class DistrictRepoTest {

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
    @DisplayName("save persists a new district and assigns a generated ID")
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
    @DisplayName("findById retrieves the persisted district by its ID")
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
    @DisplayName("findAll return List of Districts")
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
    @DisplayName("deleteById removes existing district")
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
    @DisplayName("findByName return district when exist")
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
    @DisplayName("findByName returns null when district does not exist")
    void testFindByNameNotFound() {
        District foundDistrict = districtRepo.findByName("Non-existent district");
        assertNull(foundDistrict);
    }

    @Test
    @DisplayName("findByIsDeletedFalse returns only non-deleted districts with pagination")
    void testFindByIsDeletedFalse_withPagination() {
        District d1 = new District();
        d1.setName("Alpha");
        d1.setRegency(regency);
        d1.setDeleted(false);
        entityManager.persist(d1);

        District d2 = new District();
        d2.setName("Beta");
        d2.setRegency(regency);
        d2.setDeleted(true); // soft-deleted
        entityManager.persist(d2);

        District d3 = new District();
        d3.setName("Alphaville");
        d3.setRegency(regency);
        d3.setDeleted(false);
        entityManager.persist(d3);

        entityManager.flush();

        org.springframework.data.domain.Page<District> page =
                districtRepo.findByIsDeletedFalse(org.springframework.data.domain.PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
        List<String> names = page.stream().map(District::getName).toList();
        assertTrue(names.contains("Alpha"));
        assertTrue(names.contains("Alphaville"));
    }

    @Test
    @DisplayName("findAllByRegencyId returns only districts belonging to the given regency")
    void testFindAllByRegencyId() {
        // Create a second regency to ensure filtering by regencyId works
        Province p2 = new Province();
        p2.setName("Banten");
        p2 = entityManager.persist(p2);

        Regency regency2 = new Regency();
        regency2.setName("Serang");
        regency2.setProvince(p2);
        regency2 = entityManager.persist(regency2);

        District r1d1 = new District();
        r1d1.setName("Alpha");
        r1d1.setRegency(regency);
        r1d1.setDeleted(false);
        entityManager.persist(r1d1);

        District r1d2 = new District();
        r1d2.setName("Beta");
        r1d2.setRegency(regency);
        r1d2.setDeleted(false);
        entityManager.persist(r1d2);

        District r2d1 = new District();
        r2d1.setName("Gamma");
        r2d1.setRegency(regency2);
        r2d1.setDeleted(false);
        entityManager.persist(r2d1);

        entityManager.flush();

        List<District> regency1Districts = districtRepo.findAllByRegencyId(regency.getId());
        assertEquals(2, regency1Districts.size());
        List<String> reg1Names = regency1Districts.stream().map(District::getName).toList();
        assertTrue(reg1Names.contains("Alpha"));
        assertTrue(reg1Names.contains("Beta"));

        List<District> regency2Districts = districtRepo.findAllByRegencyId(regency2.getId());
        assertEquals(1, regency2Districts.size());
        assertEquals("Gamma", regency2Districts.get(0).getName());
    }

    @Test
    @DisplayName("findByFilter performs LIKE on name and returns matching districts with pagination")
    void testFindByFilter_likeOnName_withPagination() {
        District d1 = new District();
        d1.setName("Alpha");
        d1.setRegency(regency);
        d1.setDeleted(false);
        entityManager.persist(d1);

        District d2 = new District();
        d2.setName("Alphaville");
        d2.setRegency(regency);
        d2.setDeleted(false);
        entityManager.persist(d2);

        District d3 = new District();
        d3.setName("Beta");
        d3.setRegency(regency);
        d3.setDeleted(false);
        entityManager.persist(d3);

        entityManager.flush();

        org.springframework.data.domain.Page<District> page =
                districtRepo.findByFilter("Alpha", org.springframework.data.domain.PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
        List<String> names = page.stream().map(District::getName).toList();
        assertTrue(names.contains("Alpha"));
        assertTrue(names.contains("Alphaville"));
    }



}
