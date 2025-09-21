package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.DistrictDTO;
import com.alexistdev.mykurir.v1.dto.RegencyDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.service.DistrictService;
import com.alexistdev.mykurir.v1.service.RegencyService;
import jakarta.validation.constraints.PositiveOrZero;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/api/region/district")
public class DistrictController {

    private static final String NO_DISTRICT_FOUND_MESSAGE = "No district found";

    @Autowired
    private DistrictService districtService;
    @Autowired
    private RegencyService regencyService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseData<Page<DistrictDTO>>> getAllDistricts(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<DistrictDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<District> districtPage;

        try{
            districtPage = districtService.getAllDistricts(pageable);
        } catch (RuntimeException e){
            Pageable fallbackPageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
            districtPage = districtService.getAllDistricts(fallbackPageable);
        }

        responseData.getMessages().add("No District Found");
        responseData.setStatus(false);

        if(!districtPage.isEmpty()){
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page "+ page + "of districts");
        }

        Page<DistrictDTO> districtDTOS = districtPage.map(district-> modelMapper.map(district,DistrictDTO.class));
        responseData.setPayload(districtDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseData<Page<DistrictDTO>>> getDistrictByFilter(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<DistrictDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<District> districtPage;

        try{
            districtPage = districtService.getDistrictByFilter(pageable,filter);
        } catch (RuntimeException e){
            Pageable fallbackPageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
            districtPage = districtService.getDistrictByFilter(fallbackPageable,filter);
        }
        if(!districtPage.isEmpty()){
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page "+ page + "of districts");
        }

        Page<DistrictDTO> districtDTOS = districtPage.map(district-> modelMapper.map(district,DistrictDTO.class));
        responseData.setPayload(districtDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

}
