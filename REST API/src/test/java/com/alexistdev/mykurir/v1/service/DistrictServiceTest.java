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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import java.util.Optional;

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

        @Test
        @DisplayName("Should restore district when it exists but is deleted")
        void testSaveDistric_WhenDistrictExistsAndIsDeleted() {
            existingDistrict.setDeleted(true);
            when(regencyService.findRegencyById(TEST_REGENCY_ID)).thenReturn(regency);
            when(districtRepo.findByName(TEST_DISTRICT_NAME)).thenReturn(existingDistrict);
            when(districtRepo.save(any(District.class))).thenReturn(existingDistrict);

            District result = districtService.saveDistrict(districtRequest);
            assertAll(
                    () -> assertNotNull(result),
                    () -> assertFalse(result.getDeleted()),
                    () -> assertEquals(TEST_DISTRICT_NAME, result.getName()),
                    () -> assertEquals(regency, result.getRegency())
            );

            verify(districtRepo).save(existingDistrict);
        }

        @Test
        @DisplayName("Should throw exception when district exists and is not deleted")
        void testSaveRegency_WhenDistrictExistsAndIsNotDeleted() {
            existingDistrict.setDeleted(false);

            when(regencyService.findRegencyById(TEST_REGENCY_ID)).thenReturn(regency);
            when(districtRepo.findByName(TEST_DISTRICT_NAME)).thenReturn(existingDistrict);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> districtService.saveDistrict(districtRequest));

            assertEquals("District name already exist", exception.getMessage());
            verify(districtRepo, never()).save(any());
        }

        @Test
        @DisplayName("Should create new district when it doesn't exist")
        void testSaveDistrict_WhenNewDistrict() {
            ArgumentCaptor<District> districtArgumentCaptor = ArgumentCaptor.forClass(District.class);

            when(regencyService.findRegencyById(TEST_REGENCY_ID)).thenReturn(regency);
            when(districtRepo.findByName(TEST_DISTRICT_NAME)).thenReturn(null);
            when(districtRepo.save(any(District.class))).thenAnswer(i -> i.getArgument(0));

            District result = districtService.saveDistrict(districtRequest);

            verify(districtRepo).save(districtArgumentCaptor.capture());
            District capturedDistrict = districtArgumentCaptor.getValue();

            assertAll(
                    () -> assertNotNull(result),
                    () -> assertEquals(TEST_DISTRICT_NAME,result.getName()),
                    () -> assertEquals(regency, capturedDistrict.getRegency()),
                    () -> assertFalse(capturedDistrict.getDeleted())
            );
        }

        @Test
        @DisplayName("Should throw error when regency not found")
        void testSaveDistrict_WhenRegency_NotFound() {
            districtRequest.setRegencyId(1L);
            districtRequest.setName("Bandung");

            Regency deletedRegency = new Regency();
            deletedRegency.setDeleted(true);

            when(regencyService.findRegencyById(1L)).thenReturn(deletedRegency);

            RuntimeException thrown = assertThrows(RuntimeException.class, ()->{
               districtService.saveDistrict(districtRequest);
            });

            assert "Regency Not Found".equals(thrown.getMessage());

            verify(districtRepo, never()).save(any(District.class));
        }

        @Test
        @DisplayName("Should update existing district when ID is provided")
        void testSaveDistrict_WhenIdProvided() {
            districtRequest.setId(1L);
            District currentDistrict = new District();
            currentDistrict.setId(1L);
            currentDistrict.setDeleted(true);

            when(regencyService.findRegencyById(TEST_REGENCY_ID)).thenReturn(regency);
            when(districtRepo.findByName(TEST_DISTRICT_NAME)).thenReturn(null);
            when(districtRepo.findById(1L)).thenReturn(Optional.of(currentDistrict));
            when(districtRepo.save(any(District.class))).thenAnswer(i->i.getArgument(0));

            District result = districtService.saveDistrict(districtRequest);

            assertAll(
                    ()-> assertNotNull(result),
                    ()-> assertEquals(TEST_DISTRICT_NAME, result.getName()),
                    ()-> assertEquals(regency,result.getRegency()),
                    ()-> assertFalse(result.getDeleted())
            );
        }
    }

    @Nested
    @DisplayName("Get District Test")
    class TestGetDistrict {

        @Test
        @DisplayName("Should return the expected district when requested with a specific filter")
        void testDistrict() {
            Page<District> districtPage = new PageImpl<>(Collections.singletonList(existingDistrict));

            Pageable pageable = PageRequest.of(0,10);
            when(districtRepo.findByFilter(TEST_DISTRICT_NAME.toLowerCase(), pageable)).thenReturn(districtPage);

            Page<District> result = districtService.getDistrictByFilter(pageable, TEST_DISTRICT_NAME);

            assertThat(result).isNotNull();
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).getName()).isEqualTo(TEST_DISTRICT_NAME);
        }
    }


    @Nested
    @DisplayName("Deleted District Test")
    class testDeleteDistrict {

        @Test
        @DisplayName("Should throw error when ID not found")
        void testDeleteDistrictId_NonExistingId(){
            Long nonExistingId = 2L;
            when(districtRepo.findById(nonExistingId)).thenReturn(Optional.empty());

            Exception exception = assertThrows(RuntimeException.class, ()->{
                districtService.deleteDistrictById(nonExistingId);
            });

            String expected = "District not found " +  nonExistingId;
            String actualMessage = exception.getMessage();

            assertThat(actualMessage.contains(expected));
        }

        @Test
        @DisplayName("Should delete district when Id is exists")
        void testDeleteDistrictId_ExistingId() {
            Long existingId = 1L;
            District district = new District();
            district.setId(existingId);
            district.setDeleted(false);

            when(districtRepo.findById(existingId)).thenReturn(Optional.of(district));
            when(districtRepo.save(any(District.class))).thenAnswer(i->i.getArgument(0));

            assertDoesNotThrow(() -> districtService.deleteDistrictById(existingId));

            ArgumentCaptor<District> captor = ArgumentCaptor.forClass(District.class);
            verify(districtRepo, times(1)).save(captor.capture());
            District saved = captor.getValue();

            assertEquals(existingId, saved.getId());
            assertTrue(saved.getDeleted(), "District should be marked as Deleted");
        }
    }
}
