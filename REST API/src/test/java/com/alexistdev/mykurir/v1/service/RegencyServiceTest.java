package com.alexistdev.mykurir.v1.service;

import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.models.repository.RegencyRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class RegencyServiceTest {

    @Mock
    private RegencyRepo regencyRepo;

    @InjectMocks
    private RegencyService regencyService;

    private Regency regency;
    private Page<Regency> regencyPage;
    private Pageable pageable;


    @BeforeEach
    void setUp() {
        regency = new Regency();
        regency.setId(1L);
        regency.setName("Test Regency");
        regency.setDeleted(false);

        pageable = PageRequest.of(0, 10);
        regencyPage = new PageImpl<>(List.of(regency));

    }

    @Test
    void getAllRegencies_ShouldReturnPageOfRegencies() {
        // Given
        when(regencyRepo.findByIsDeletedFalse(pageable)).thenReturn(regencyPage);

        // When
        Page<Regency> result = regencyService.getAllRegencies(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(regency.getId(), result.getContent().get(0).getId());
        assertEquals(regency.getName(), result.getContent().get(0).getName());
    }

}
