package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegencyServiceTest {

    @Mock
    private RegencyRepo regencyRepo;

    @InjectMocks
    private RegencyService regencyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRegencies() {
        Province province1 = new Province();
        province1.setId(1L);
        province1.setName("Jakarta");

        Province province2 = new Province();
        province2.setId(2L);
        province2.setName("Lampung");

        Regency regency1 = new Regency();
        regency1.setId(1L);
        regency1.setName("Jakarta Selatan");
        regency1.setProvince(province1);

        Regency regency2 = new Regency();
        regency2.setId(2L);
        regency2.setName("Pringsewu");
        regency2.setProvince(province2);

        List<Regency> regencies = Arrays.asList(regency1,regency2);

        when(regencyRepo.findAll()).thenReturn(regencies);

        List<Regency> result = regencyService.getAllRegencies();

        assertEquals(2,result.size());
        assertEquals("Jakarta Selatan",result.get(0).getName());
        assertEquals("Jakarta",result.get(0).getProvince().getName());
        assertEquals("Pringsewu",result.get(1).getName());
        assertEquals("Lampung",result.get(1).getProvince().getName());

        verify(regencyRepo,times(1)).findAll();
    }

    @Test
    void testGetAllRegenciesEmptyList() {

        when(regencyRepo.findAll()).thenReturn(List.of());
        List<Regency> result = regencyService.getAllRegencies();
        assertTrue(result.isEmpty());
        verify(regencyRepo,times(1)).findAll();
    }
}
