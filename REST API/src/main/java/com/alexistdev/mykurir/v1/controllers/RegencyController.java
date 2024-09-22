package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.service.RegencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/region/regency")
public class RegencyController {

    @Autowired
    private RegencyService regencyService;

    @GetMapping
    public ResponseEntity<ResponseData<List<Regency>>> getAllRegencies() {
        ResponseData<List<Regency>> responseData = new ResponseData<>();
        List<Regency> regencies = regencyService.getAllRegencies();
        responseData.getMessages().add("No Regency found");
        if(regencies != null && !regencies.isEmpty()) {
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("get all regencies returned " + regencies.size());
        }
        responseData.setPayload(regencies);
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
