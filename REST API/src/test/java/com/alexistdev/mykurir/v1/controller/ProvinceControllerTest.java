package com.alexistdev.mykurir.v1.controller;

import com.alexistdev.mykurir.v1.dto.ProvinceDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.request.ProvinceRequest;
import com.alexistdev.mykurir.v1.service.ProvinceService;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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

//    @Test
//    void testGetAllProvinces() {
//        Province province1 = new Province();
//        Province province2 = new Province();
//
//        List<Province> provinces = Arrays.asList(province1,province2);
//
//        when(provinceService.getAllProvinces()).thenReturn(provinces);
//
//        ResponseEntity<ResponseData<List<ProvinceDTO>>> response = provinceController.getAllProvinces();
//
//        assertEquals(HttpStatus.OK,response.getStatusCode());
//        assertEquals(2, Objects.requireNonNull(response.getBody()).getPayload().size());
//        assertEquals("get all provincies returned 2",response.getBody().getMessages().getFirst());
//        assertTrue(response.getBody().isStatus());
//    }

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
