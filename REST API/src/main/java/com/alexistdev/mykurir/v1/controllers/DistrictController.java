package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.DistrictDTO;
import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.District;
import com.alexistdev.mykurir.v1.request.DistrictRequest;
import com.alexistdev.mykurir.v1.service.DistrictService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
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
@RequestMapping("/v1/api/region/district")
public class DistrictController {

    private static final String NO_DISTRICT_FOUND_MESSAGE = "No District found";
    private static final String DISTRICT_SAVED_MESSAGE = "District Saved";
    private static final String DISTRICT_DELETED_MESSAGE = "District successfully deleted!";
    private static final String NO_ID_DISTRICT_FOUND_MESSAGE = "Id cannot be null!";

    @Autowired
    private DistrictService districtService;

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

        responseData.getMessages().add(NO_DISTRICT_FOUND_MESSAGE);
        responseData.setStatus(false);

        handleNonEmptyPage(responseData,districtPage,page);

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
        responseData.getMessages().add("No District found");
        responseData.setStatus(false);

        if (!districtPage.isEmpty()) {
            responseData.setStatus(true);
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("Retrieved page " + page + " of districts"
            );
        }

        Page<DistrictDTO> districtDTOS = districtPage.map(district-> modelMapper.map(district,DistrictDTO.class));
        responseData.setPayload(districtDTOS);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData<District>> addDistrict(@Valid @RequestBody DistrictRequest request, Errors errors) {
        ResponseData<District> responseData = new ResponseData<>();
        responseData.setStatus(false);

        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try{
            District savedDistrict = districtService.saveDistrict(request);
            responseData.setStatus(true);
            responseData.getMessages().add(DISTRICT_SAVED_MESSAGE);
            responseData.setPayload(savedDistrict);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (RuntimeException e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @PatchMapping
    public ResponseEntity<ResponseData<District>> updateDistrict(@Valid @RequestBody DistrictRequest request, Errors errors) {
        ResponseData<District> responseData = new ResponseData<>();
        responseData.setStatus(false);

        ResponseEntity<ResponseData<District>> idValidationResponse = validateRequestId(responseData, request);
        if (idValidationResponse != null) {
            return idValidationResponse;
        }

        if (errors.hasErrors()) {
            processErrors(errors, responseData);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        District districtFound = districtService.findDistrictById(request.getId());
        if(districtFound == null){
            responseData.getMessages().add(NO_DISTRICT_FOUND_MESSAGE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try{
            District savedDistrict = districtService.updatedDistrict(request);
            responseData.setStatus(true);
            responseData.getMessages().add(DISTRICT_SAVED_MESSAGE);
            responseData.setPayload(savedDistrict);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        } catch (RuntimeException e){
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteDistrict(@PathVariable("id")  Long id){
        ResponseData<Void> responseData = new ResponseData<>();
        try{
            districtService.deleteDistrictById(id);
            responseData.getMessages().add(String.format(DISTRICT_DELETED_MESSAGE));
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

    private <T> void handleNonEmptyPage(ResponseData<Page<T>> responseData, Page<?> pageResult, int pageNumber){
        if (!pageResult.isEmpty()) {
            responseData.setStatus(true);
            if (!responseData.getMessages().isEmpty()) {
                responseData.getMessages().removeFirst();
            }
            responseData.getMessages().add("Retrieved page " + pageNumber + " of districts");
        }
    }

    private <T> ResponseEntity<ResponseData<T>> validateRequestId(ResponseData<T> responseData, DistrictRequest request){
        if(request.getId() == null){
            responseData.getMessages().add(NO_ID_DISTRICT_FOUND_MESSAGE);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        return null;
    }

}
