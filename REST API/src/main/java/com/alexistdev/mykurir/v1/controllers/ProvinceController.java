package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ProvinceDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.masterconstant.Validation;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.request.ProvinceRequest;
import com.alexistdev.mykurir.v1.service.ProvinceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public ResponseEntity<ResponseData<Page<ProvinceDTO>>> getAllProvinces(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<ProvinceDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));


        Page<Province> provincesPage = provinceService.getAllProvinces(pageable);

        responseData.getMessages().add("No province found");
        responseData.setStatus(false);

        if (!provincesPage.isEmpty()) {
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page " + page + " of provincies");
        }
        Page<ProvinceDTO> provinceDTOS = provincesPage
                .map(province -> modelMapper.map(province,ProvinceDTO.class));
        responseData.setPayload(provinceDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @GetMapping("/filter")
    public ResponseEntity<ResponseData<Page<ProvinceDTO>>> getUserByFilter(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<ProvinceDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Province> provincesPage = provinceService.getProvinceByFilter(pageable, filter);

        responseData.getMessages().add("No province found");
        responseData.setStatus(false);
        if (!provincesPage.isEmpty()) {
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page " + page + " of provinces"
            );
        }
        Page<ProvinceDTO> provinceDTOS = provincesPage
                .map(province -> modelMapper.map(province, ProvinceDTO.class));
        responseData.setPayload(provinceDTOS);
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

        try {
            Province result = provinceService.saveProvince(modelMapper.map(request, Province.class));
            responseData.setPayload(result);
            responseData.getMessages().add(Validation.success("province"));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @PatchMapping
    public ResponseEntity<ResponseData<Province>> updateProvince(@Valid @RequestBody ProvinceRequest request, Errors errors) {
        ResponseData<Province> responseData = new ResponseData<>();
        responseData.setStatus(false);
        if(request.getId() == null){
            responseData.getMessages().add("Id cannot be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Province provinceFound = provinceService.findProvinceById(request.getId());
        if(provinceFound == null) {
            responseData.getMessages().add("Province Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try{
            Province updatedProvince = provinceService.saveProvince(modelMapper.map(request, Province.class));
            responseData.setPayload(updatedProvince);
            responseData.getMessages().add(Validation.success("province"));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteProvince(@PathVariable("id") Long id) {
        ResponseData<Void> responseData = new ResponseData<>();
        try{
            provinceService.deleteProvinceById(id);
            responseData.getMessages().add(String.format("Province successfully deleted!"));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e){
            responseData.getMessages().add(e.getMessage());
            responseData.setStatus(false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    private void processErrors(Errors errors, ResponseData<?> responseData) {
        for (ObjectError error : errors.getAllErrors()) {
            responseData.getMessages().add(error.getDefaultMessage());
        }
    }


}
