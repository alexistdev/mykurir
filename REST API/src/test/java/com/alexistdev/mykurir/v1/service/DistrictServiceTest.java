package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.DistrictRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class DistrictServiceTest {

    @Mock
    private DistrictRepo districtRepo;

    @InjectMocks
    private DistrictService districtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDistricts() {
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

        District district1 = new District();
        district1.setId(1L);
        district1.setRegency(regency1);
        district1.setName("Setiabudi");

        District district2 = new District();
        district2.setId(2L);
        district2.setRegency(regency2);
        district2.setName("Pagelaran");

        List<District> districts = List.of(district1, district2);

        when(districtRepo.findAll()).thenReturn(districts);

        List<District> result = districtService.getAllDistricts();

        assertEquals(2,districts.size());
        assertEquals("Setiabudi",result.get(0).getName());
        assertEquals("Jakarta Selatan", result.get(0).getRegency().getName());
        assertEquals("Jakarta", result.get(0).getRegency().getProvince().getName());

        assertEquals("Pagelaran",result.get(1).getName());
        assertEquals("Pringsewu", result.get(1).getRegency().getName());
        assertEquals("Lampung", result.get(1).getRegency().getProvince().getName());

        verify(districtRepo,times(1)).findAll();
    }

    @Test
    void testGetAllDistrictsEmptyList() {
        when(districtRepo.findAll()).thenReturn(List.of());
        List<District> result = districtService.getAllDistricts();
        assertTrue(result.isEmpty());
        verify(districtRepo,times(1)).findAll();
    }
}
