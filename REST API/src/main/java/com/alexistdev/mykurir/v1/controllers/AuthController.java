package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.models.repository.UserRepo;
import com.alexistdev.mykurir.v1.request.LoginRequest;
import com.alexistdev.mykurir.v1.request.RegisterRequest;
import com.alexistdev.mykurir.v1.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200/login")
@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/testing")
    public ResponseEntity<ResponseData<String>> testing(){
        ResponseData<String> responseData = new ResponseData<>();
        responseData.setStatus(true);
        responseData.setPayload("okay ini keren");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseData<User>> register(@Valid  @RequestBody RegisterRequest userRequest, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        handleErrors(errors, responseData);

        if(!responseData.isStatus()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        try {
            User user = modelMapper.map(userRequest, User.class);
            responseData.setPayload(userService.registerUser(user));
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        }catch (Exception e) {
            responseData.setStatus(false);
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }

    @PutMapping("/register/{id}")
    public ResponseEntity<ResponseData<User>> update(@Valid  @RequestBody RegisterRequest userRequest, @PathVariable long id, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        handleErrors(errors, responseData);

        if(!responseData.isStatus()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        responseData.setStatus(false);
        try {
            responseData.getMessages().add("Success Update data");
            User user = userService.findById(id);
            if(user == null){
                responseData.getMessages().removeFirst();
                responseData.getMessages().add("NOT FOUND");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
            }

            User result = userService.updateUser(user,userRequest);

            responseData.setPayload(result);
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseData);
        }catch (Exception e) {
            responseData.getMessages().removeFirst();
            responseData.getMessages().add(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseData<User>> login(@Valid @RequestBody LoginRequest loginRequest, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        handleErrors(errors, responseData);

        User user = userService.authenticate(loginRequest);

        if (user != null) {
            responseData.setPayload(user);
            responseData.getMessages().add("User is valid");
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }

        responseData.getMessages().add("Invalid username or password");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }

    private void handleErrors(Errors errors, ResponseData<?> responseData){
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
        } else {
            responseData.setStatus(true);
        }
    }
}
