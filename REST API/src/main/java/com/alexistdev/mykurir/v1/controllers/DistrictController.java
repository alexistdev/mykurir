package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/region/district")
public class DistrictController {

    private static final String NO_DISTRICT_FOUND_MESSAGE = "No district found";

    @Autowired
    private DistrictService districtService;

    @GetMapping
    public ResponseEntity<ResponseData<List<District>>> getAllDistricts() {
        ResponseData<List<District>> responseData = new ResponseData<>();
        List<District> districts = districtService.getAllDistricts();
        responseData.getMessages().add(generateMessage(districts));
        responseData.setPayload(districts);
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    private String generateMessage(List<District> districts){
        if (districts != null && !districts.isEmpty()) {
            return "get all districts returned " + districts.size();
        }
        return NO_DISTRICT_FOUND_MESSAGE;
    }
}
