package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/region/province")
public class ProvinceController {

    @Autowired
    private ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<ResponseData<List<Province>>> getAllProvinces() {
        ResponseData<List<Province>> responseData = new ResponseData<>();
        List<Province> provinces = provinceService.getAllProvinces();
        responseData.getMessages().add("No provincy found");
        if(provinces != null && !provinces.isEmpty()) {
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("get all provincies returned " + provinces.size());
        }
        responseData.setPayload(provinces);
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }


//    @PostMapping
//    public ResponseEntity<ResponseData<Province>> addProvince(@RequestBody Province province) {
//
//    }
}
