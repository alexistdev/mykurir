package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.masterconstant.Validation;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.repository.ProvinceRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProvinceServiceTest {
    @Mock
    private ProvinceRepo provinceRepo;

    @InjectMocks
    private ProvinceService provinceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProvinces() {
        Province province1 = new Province();
        province1.setId(1L);
        province1.setName("Lampung");

        Province province2 = new Province();
        province2.setId(2L);
        province2.setName("Jakarta");

        List<Province> provinces = Arrays.asList(province1,province2);

        when(provinceRepo.findAll()).thenReturn(provinces);

        List<Province> result = provinceService.getAllProvinces();

        assertEquals(2,result.size());
        assertEquals("Lampung",result.get(0).getName());
        assertEquals("Jakarta",result.get(1).getName());
    }

    @Test
    void testAddNewProvince() {
        Province newProvince = new Province();
        newProvince.setName("Lampung");

        when(provinceRepo.findByName("Lampung")).thenReturn(null);
        when(provinceRepo.save(any(Province.class))).thenReturn(newProvince);

        Province result = provinceService.addProvince(newProvince);

        assertNotNull(result);
        assertEquals("Lampung",result.getName());
        verify(provinceRepo,times(1)).save(any(Province.class));
    }

    @Test
    void testUpdateExistingProvince() {
        Province existingProvince = new Province();
        existingProvince.setId(1L);
        existingProvince.setName("Lampung");

        Province updatedProvince = new Province();
        updatedProvince.setId(1L);
        updatedProvince.setName("Jakarta");

        when(provinceRepo.findById(1L)).thenReturn(Optional.of(existingProvince));
        when(provinceRepo.findByName("Jakarta")).thenReturn(null);
        when(provinceRepo.save(any(Province.class))).thenReturn(updatedProvince);

        Province result = provinceService.addProvince(updatedProvince);
        assertNotNull(result);
        assertEquals("Jakarta",result.getName());
        verify(provinceRepo,times(1)).save(any(Province.class));
    }

    @Test
    void testAddProvinceWithExistingName() {
        Province newProvince = new Province();
        newProvince.setName("Lampung");

        when(provinceRepo.findByName("Lampung")).thenReturn(newProvince);
        Exception exception = assertThrows(RuntimeException.class,() ->{
            provinceService.addProvince(newProvince);
        });
        String expectedMessage = Validation.nameExist("Province");
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}