package com.alexistdev.mykurir.v1.controllers;

import com.alexistdev.mykurir.v1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/users")
public class AuthController {

    @Autowired
    private UserService userService;


}
