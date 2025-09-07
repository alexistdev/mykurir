package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.DistrictRepo;
import com.alexistdev.mykurir.v1.request.DistrictRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

    @Mock
    private RegencyService regencyService;

    @InjectMocks
    private DistrictService districtService;

    private DistrictRequest districtRequest;
    private Province province;
    private Regency regency;
    private District existingDistrict;
    private static final String TEST_DISTRICT_NAME = "Test District";
    private static final Long TEST_REGENCY_ID = 1L;
    private static final Long TEST_PROVINCE_ID = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        districtRequest = new DistrictRequest();
        districtRequest.setName(TEST_DISTRICT_NAME);
        districtRequest.setRegencyId(TEST_REGENCY_ID);

        // Initialize Province
        province = new Province();
        province.setId(TEST_PROVINCE_ID);
        province.setName("Test Province");
        province.setDeleted(false);

        // Initialize existing Regency
        regency = new Regency();
        regency.setId(TEST_REGENCY_ID);
        regency.setProvince(province);
        regency.setName("Test Regency");

        // Initialize existing District

        existingDistrict = new District();
        existingDistrict.setName(TEST_DISTRICT_NAME);
        existingDistrict.setRegency(regency);
        existingDistrict.setDeleted(false);

    }

    @Nested
    @DisplayName("Save District Tests")
    class SaveDistrictTest {

        @Test
        @DisplayName("Should throw exception when regency not found")
        void testSaveDistrict_WhenRegencyNotFound() {
            when(regencyService.findRegencyById(TEST_REGENCY_ID)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> districtService.saveDistrict(districtRequest));

            assertEquals("Regency Not Found", exception.getMessage());
            verify(regencyService).findRegencyById(TEST_REGENCY_ID);
            verifyNoInteractions(districtRepo);
        }
    }
}
