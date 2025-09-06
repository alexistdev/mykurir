package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import com.alexistdev.mykurir.v1.request.RegencyRequest;
import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        MockitoAnnotations.openMocks(this);
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
        @DisplayName("Should throw error when province not found")
        void testSaveRegency_WhenProvince_NotFound() {
            regencyRequest.setProvinceId(1L);
            regencyRequest.setName("Jawa Barat");

            Province deletedProvince = new Province();
            deletedProvince.setDeleted(true);

            when(provinceService.findProvinceById(1L)).thenReturn(deletedProvince);

            RuntimeException thrown = assertThrows(RuntimeException.class, ()->{
                regencyService.saveRegency(regencyRequest);
            });

            assert "Province Not Found".equals(thrown.getMessage());

            verify(regencyRepo, never()).save(any(Regency.class));
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

    @Nested
    @DisplayName("Get Regency Test")
    class GetRegencyTests {

        @Test
        @DisplayName("Should return the expected regency when requested with a specific filter")
        void testGetRegencyByFilter() {
            Page<Regency> regencyPage = new PageImpl<>(Collections.singletonList(existingRegency));

            Pageable pageable = PageRequest.of(0, 10);
            when(regencyRepo.findByFilter(TEST_REGENCY_NAME.toLowerCase(), pageable)).thenReturn(regencyPage);

            Page<Regency> result = regencyService.getRegencyByFilter(pageable, TEST_REGENCY_NAME);

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo(TEST_REGENCY_NAME);
        }
    }

    @Test
    void testDeleteRegencyId_NonExistingId() {
        Long nonExistingId = 2L;
        when(regencyRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,()->{
            regencyService.deleteRegencyById(nonExistingId);
        });

        String expected = "Regency not found " + nonExistingId;
        String actualMessage =  exception.getMessage();

        assertTrue(actualMessage.contains(expected));
    }

    @Test
    void testDeleteRegencyId_ExistingId() {
        Long existingId = 1L;
        Regency regency = new Regency();
        regency.setId(existingId);
        regency.setDeleted(false);

        when(regencyRepo.findById(existingId)).thenReturn(Optional.of(regency));
        when(regencyRepo.save(any(Regency.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> regencyService.deleteRegencyById(existingId));

        ArgumentCaptor<Regency> captor = ArgumentCaptor.forClass(Regency.class);
        verify(regencyRepo, times(1)).save(captor.capture());
        Regency saved = captor.getValue();

        assertEquals(existingId, saved.getId());
        assertTrue(saved.getDeleted(), "Regency should be marked as Deleted");
    }
}
