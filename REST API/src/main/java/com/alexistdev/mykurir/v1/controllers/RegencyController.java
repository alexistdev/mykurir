package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.RegencyDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.service.RegencyService;
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
    public ResponseEntity<ResponseData<Page<RegencyDTO>>> getAllRegencies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<RegencyDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Regency> regenciesPage = regencyService.getAllRegencies(pageable);

        responseData.getMessages().add("No Regency found");
        responseData.setStatus(false);

        if(!regenciesPage.isEmpty()){
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page " + page + "of regencies");
        }

        Page<RegencyDTO> regencyDTOS = regenciesPage
                .map( regency -> modelMapper.map(regency,RegencyDTO.class));

        responseData.setPayload(regencyDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }
}
