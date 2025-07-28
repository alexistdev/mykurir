package com.alexistdev.mykurir.v1.controller;

import com.alexistdev.mykurir.v1.dto.ProvinceDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.request.ProvinceRequest;
import com.alexistdev.mykurir.v1.service.ProvinceService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alexistdev.mykurir.v1.controllers.ProvinceController;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class ProvinceControllerTest {

    private ProvinceService provinceService;

    private ModelMapper modelMapper;

    private ProvinceController provinceController;

    @BeforeEach
    void setUp() {
        provinceService = mock(ProvinceService.class);
        modelMapper = mock(ModelMapper.class);
        provinceController = new ProvinceController(provinceService,modelMapper);
    }

    @Test
    void testGetAllProvinces() {
        Province province1 = new Province();
        Province province2 = new Province();
        List<Province> provinces = Arrays.asList(province1, province2);
        Page<Province> provincePage = new PageImpl<>(provinces);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        when(provinceService.getAllProvinces(pageable)).thenReturn(provincePage);

        ProvinceDTO dto1 = new ProvinceDTO();
        ProvinceDTO dto2 = new ProvinceDTO();
        when(modelMapper.map(province1, ProvinceDTO.class)).thenReturn(dto1);
        when(modelMapper.map(province2, ProvinceDTO.class)).thenReturn(dto2);

        ResponseEntity<ResponseData<Page<ProvinceDTO>>> response =
                provinceController.getAllProvinces(0, 10, "id", "asc");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isStatus());
        assertEquals("Retrieved page 0 of provincies", response.getBody().getMessages().getFirst());
        assertEquals(2, response.getBody().getPayload().getContent().size());

        verify(provinceService).getAllProvinces(pageable);
        verify(modelMapper, times(2)).map(any(Province.class), eq(ProvinceDTO.class));

    }

    @Test
    void testAddProvince_ValidRequest() {
        ProvinceRequest request = mock(ProvinceRequest.class);
        Province province = new Province();
        Province savedProvince = new Province();

        Errors errors = new BeanPropertyBindingResult(request,"request");

        when(modelMapper.map(request,Province.class)).thenReturn(province);
        when(provinceService.saveProvince(province)).thenReturn(savedProvince);

        ResponseEntity<ResponseData<Province>> response = provinceController.addProvince(request,errors);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(savedProvince, Objects.requireNonNull(response.getBody()).getPayload());
        assertTrue(response.getBody().isStatus());
    }

    @Test
    void testAddProvince_InvalidRequest() {
        ProvinceRequest request = new ProvinceRequest();
        Errors errors = new BeanPropertyBindingResult(request,"request");
        errors.rejectValue("name","name.empty","The province name must not be empty");

        ResponseEntity<ResponseData<Province>> response = provinceController.addProvince(request,errors);

        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        assertEquals("The province name must not be empty", Objects.requireNonNull(response.getBody()).getMessages().getFirst());
        assertFalse(response.getBody().isStatus());

    }

    @Test
    void testAddProvince_NullRequest() {
        Errors mockErrors = mock(Errors.class);
        when(mockErrors.hasErrors()).thenReturn(true);

        ResponseEntity<ResponseData<Province>> response = provinceController.addProvince(null, mockErrors);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertFalse(Objects.requireNonNull(response.getBody()).isStatus());
    }

    @Test
    void testAddProvince_EmptyName() {
        ProvinceRequest request = new ProvinceRequest();
        Errors errors = new BeanPropertyBindingResult(request, "request");
        errors.rejectValue("name", "name.empty", "The province name must not be empty");

        ResponseEntity<ResponseData<Province>> response = provinceController.addProvince(request, errors);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The province name must not be empty", Objects.requireNonNull(response.getBody()).getMessages().getFirst());
        assertFalse(response.getBody().isStatus());
    }

    @Test
    void testAddProvince_LongName() {
        ProvinceRequest request = new ProvinceRequest();
        request.setName("A very long name that is more than the allowed length of 150 characters. A very long name that is more than the allowed length of 150 characters. this sentence total 181 characters.");

        Errors errors = new BeanPropertyBindingResult(request, "request");

        errors.rejectValue("name", "Size", "The length of the name must be between 1 and 150 characters");

        ResponseEntity<ResponseData<Province>> response = provinceController.addProvince(request, errors);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(Objects.requireNonNull(response.getBody()).getMessages().getFirst().contains("The length of the name must be between 1 and 150 characters"));
        assertFalse(response.getBody().isStatus());
    }
}
