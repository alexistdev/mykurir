package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.request.LoginRequest;
import com.alexistdev.mykurir.v1.request.RegisterRequest;
import com.alexistdev.mykurir.v1.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/register")
    public ResponseEntity<ResponseData<User>> register(@Valid  @RequestBody RegisterRequest userRequest, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        responseData.setPayload(null);
        responseData.setStatus(false);
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }

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

    @PostMapping("/login")
    public ResponseEntity<ResponseData<User>> login(@Valid @RequestBody LoginRequest loginRequest, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        responseData.setPayload(null);
        responseData.setStatus(false);
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessages().add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        User user = userService.authenticate(loginRequest);
        if (user != null) {
            responseData.setPayload(user);
            responseData.setStatus(true);
            return ResponseEntity.status(HttpStatus.OK).body(responseData);
        }
        responseData.getMessages().add("Invalid username or password");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
    }
}
