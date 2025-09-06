package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ProvinceDTO;
import com.alexistdev.mykurir.v1.dto.RegencyDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.masterconstant.Validation;
import com.alexistdev.mykurir.v1.models.entity.Province;
import com.alexistdev.mykurir.v1.models.entity.Regency;
import com.alexistdev.mykurir.v1.request.RegencyRequest;
import com.alexistdev.mykurir.v1.service.RegencyService;
import jakarta.validation.Valid;
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

    @GetMapping("/filter")
    public ResponseEntity<ResponseData<Page<RegencyDTO>>> getRegencyByFilter(
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        ResponseData<Page<RegencyDTO>> responseData = new ResponseData<>();
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Regency> regenciesPage = regencyService.getRegencyByFilter(pageable, filter);

        responseData.getMessages().add("No regency found");
        responseData.setStatus(false);
        if (!regenciesPage.isEmpty()) {
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page " + page + " of regencies"
            );
        }
        Page<RegencyDTO> regencyDTOS = regenciesPage
                .map(regency-> modelMapper.map(regency, RegencyDTO.class));
        responseData.setPayload(regencyDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Regency>> addRegency(@Valid @RequestBody RegencyRequest request, Errors errors) {
        ResponseData<Regency> responseData = new ResponseData<>();
        responseData.setStatus(false);
        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            Regency result = regencyService.saveRegency(request);
            responseData.setPayload(result);
            responseData.getMessages().add(Validation.success("regency"));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @PatchMapping
    public ResponseEntity<ResponseData<Regency>> updateRegency(@Valid @RequestBody RegencyRequest request, Errors errors) {
        ResponseData<Regency> responseData = new ResponseData<>();
        responseData.setStatus(false);

        if(request.getId() == null){
            responseData.getMessages().add("Id cannot be null");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Regency regencyFound = regencyService.findRegencyById(request.getId());
        if(regencyFound == null){
            responseData.getMessages().add("Regency Not Found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try{
            Regency updatedRegency = regencyService.saveRegency(request);
            responseData.setPayload(updatedRegency);
            responseData.getMessages().add(Validation.success("regency"));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (Exception e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteRegency(@PathVariable("id") Long id) {
        ResponseData<Void> responseData = new ResponseData<>();
        try{
            regencyService.deleteRegencyById(id);
            responseData.getMessages().add(String.format("Regency successfully deleted!"));
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
