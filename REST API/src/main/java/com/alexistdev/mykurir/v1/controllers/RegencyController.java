package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.RegencyDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.service.RegencyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/api/region/regency")
public class RegencyController {

    @Autowired
    private RegencyService regencyService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<List<RegencyDTO>>> getAllRegencies() {
        ResponseData<List<RegencyDTO>> responseData = new ResponseData<>();
        List<Regency> regencies = regencyService.getAllRegencies();
        responseData.getMessages().add("No Regency found");
        responseData.setPayload(Collections.emptyList());

        if(regencies != null && !regencies.isEmpty()) {
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("get all regencies returned " + regencies.size());

            List<RegencyDTO> regencyDTOS = regencies.stream()
                    .map(regency -> modelMapper.map(regency, RegencyDTO.class))
                    .toList();
            responseData.setPayload(regencyDTOS);
        }

        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
