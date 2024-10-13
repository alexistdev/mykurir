package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.masterconstant.Validation;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.request.ProvinceRequest;
import com.alexistdev.mykurir.v1.service.ProvinceService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/region/province")
public class ProvinceController {

    private final ProvinceService provinceService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProvinceController(ProvinceService provinceService, ModelMapper modelMapper) {
        this.provinceService = provinceService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<Province>>> getAllProvinces() {
        ResponseData<List<Province>> responseData = new ResponseData<>();
        List<Province> provinces = provinceService.getAllProvinces();
        responseData.getMessages().add("No province found");

        if (provinces != null && !provinces.isEmpty()) {
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("get all provincies returned " + provinces.size());
        }

        responseData.setPayload(provinces);
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


    @PostMapping
    public ResponseEntity<ResponseData<Province>> addProvince(@Valid @RequestBody ProvinceRequest request, Errors errors) {
        ResponseData<Province> responseData = new ResponseData<>();
        responseData.setStatus(false);

        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Province result = provinceService.addProvince(modelMapper.map(request, Province.class));
        responseData.setPayload(result);
        responseData.getMessages().add(Validation.success("province"));
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    private void processErrors(Errors errors, ResponseData<?> responseData) {
        for (ObjectError error : errors.getAllErrors()) {
            responseData.getMessages().add(error.getDefaultMessage());
        }
    }
}
