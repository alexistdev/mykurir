package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import com.alexistdev.mykurir.v1.request.RegencyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RegencyServiceTest {
    @Mock
    private RegencyRepo regencyRepo;

    @Mock
    private ProvinceService provinceService;

    @InjectMocks
    private RegencyService regencyService;

    private RegencyRequest regencyRequest;
    private Province province;
    private Regency existingRegency;
    private static final String TEST_REGENCY_NAME = "Test Regency";
    private static final Long TEST_PROVINCE_ID = 1L;

    @BeforeEach
    void setUp() {
        // Initialize RegencyRequest
        regencyRequest = new RegencyRequest();
        regencyRequest.setName(TEST_REGENCY_NAME);
        regencyRequest.setProvinceId(TEST_PROVINCE_ID);

        // Initialize Province
        province = new Province();
        province.setId(TEST_PROVINCE_ID);
        province.setName("Test Province");

        // Initialize existing Regency
        existingRegency = new Regency();
        existingRegency.setName(TEST_REGENCY_NAME);
        existingRegency.setProvince(province);
    }

    @Nested
    @DisplayName("Save Regency Tests")
    class SaveRegencyTests {

        @Test
        @DisplayName("Should throw exception when province not found")
        void testSaveRegency_WhenProvinceNotFound() {
            when(provinceService.findProvinceById(TEST_PROVINCE_ID)).thenReturn(null);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> regencyService.saveRegency(regencyRequest));

            assertEquals("Province Not Found", exception.getMessage());
            verify(provinceService).findProvinceById(TEST_PROVINCE_ID);
            verifyNoInteractions(regencyRepo);
        }

        @Test
        @DisplayName("Should restore regency when it exists but is deleted")
        void testSaveRegency_WhenRegencyExistsAndIsDeleted() {
            existingRegency.setDeleted(true);

            when(provinceService.findProvinceById(TEST_PROVINCE_ID)).thenReturn(province);
            when(regencyRepo.findByName(TEST_REGENCY_NAME)).thenReturn(existingRegency);
            when(regencyRepo.save(any(Regency.class))).thenReturn(existingRegency);

            Regency result = regencyService.saveRegency(regencyRequest);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertFalse(result.getDeleted()),
                    () -> assertEquals(TEST_REGENCY_NAME, result.getName()),
                    () -> assertEquals(province, result.getProvince())
            );

            verify(regencyRepo).save(existingRegency);
        }

        @Test
        @DisplayName("Should throw exception when regency exists and is not deleted")
        void testSaveRegency_WhenRegencyExistsAndIsNotDeleted() {
            existingRegency.setDeleted(false);

            when(provinceService.findProvinceById(TEST_PROVINCE_ID)).thenReturn(province);
            when(regencyRepo.findByName(TEST_REGENCY_NAME)).thenReturn(existingRegency);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> regencyService.saveRegency(regencyRequest));

            assertEquals("Regency name already exist", exception.getMessage());
            verify(regencyRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should create new regency when it doesn't exist")
        void testSaveRegency_WhenNewRegency() {
            ArgumentCaptor<Regency> regencyCaptor = ArgumentCaptor.forClass(Regency.class);

            when(provinceService.findProvinceById(TEST_PROVINCE_ID)).thenReturn(province);
            when(regencyRepo.findByName(TEST_REGENCY_NAME)).thenReturn(null);
            when(regencyRepo.save(any(Regency.class))).thenAnswer(i -> i.getArgument(0));

            Regency result = regencyService.saveRegency(regencyRequest);

            verify(regencyRepo).save(regencyCaptor.capture());
            Regency capturedRegency = regencyCaptor.getValue();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(TEST_REGENCY_NAME, result.getName()),
                    () -> assertEquals(province, capturedRegency.getProvince()),
                    () -> assertFalse(capturedRegency.getDeleted())
            );
        }

        @Test
        @DisplayName("Should update existing regency when ID is provided")
        void testSaveRegency_WhenIdProvided() {
            regencyRequest.setId(1L);
            Regency currentRegency = new Regency();
            currentRegency.setId(1L);
            currentRegency.setDeleted(true);

            when(provinceService.findProvinceById(TEST_PROVINCE_ID)).thenReturn(province);
            when(regencyRepo.findByName(TEST_REGENCY_NAME)).thenReturn(null);
            when(regencyRepo.findById(1L)).thenReturn(Optional.of(currentRegency));
            when(regencyRepo.save(any(Regency.class))).thenAnswer(i -> i.getArgument(0));

            Regency result = regencyService.saveRegency(regencyRequest);

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(TEST_REGENCY_NAME, result.getName()),
                    () -> assertEquals(province, result.getProvince()),
                    () -> assertFalse(result.getDeleted())
            );
        }
    }

}
