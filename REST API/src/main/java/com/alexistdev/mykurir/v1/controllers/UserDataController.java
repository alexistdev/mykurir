package com.alexistdev.mykurir.v1.controllers;


import com.alexistdev.mykurir.v1.dto.ResponseData;
import com.alexistdev.mykurir.v1.dto.UserDTO;
import com.alexistdev.mykurir.v1.models.entity.User;
import com.alexistdev.mykurir.v1.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/v1/api/users")
public class UserDataController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping("/get_all_users")
    public ResponseEntity<ResponseData<List<UserDTO>>> getAllUserData() throws ExecutionException, InterruptedException {
        ResponseData<List<UserDTO>> responseData = new ResponseData<>();
        List<User> users = userService.getAllUsers();
        responseData.getMessages().add("No users found");
        if(!users.isEmpty()){
            responseData.getMessages().removeFirst();
            responseData.getMessages().add("get all users returned " + users.size());
        }

        List<UserDTO> userDTOS = users.stream()
                        .map(user -> modelMapper.map(user, UserDTO.class))
                                .toList();

        responseData.setPayload(userDTOS);
        responseData.setStatus(true);
        return ResponseEntity.status(HttpStatus.OK).body(responseData);
    }

}
