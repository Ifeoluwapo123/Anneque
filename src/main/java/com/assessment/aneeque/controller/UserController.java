package com.assessment.aneeque.controller;

import com.assessment.aneeque.payload.request.LoginRequest;
import com.assessment.aneeque.payload.request.RegistrationRequest;
import com.assessment.aneeque.payload.response.LoginResponse;
import com.assessment.aneeque.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "user/login")
    public ResponseEntity<LoginResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }

    @PostMapping(value = "user/register")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationRequest registrationRequest){
        return userService.registerUser(registrationRequest);
    }

    @GetMapping(value = "users/{userId}")
    public ResponseEntity getUserById(@PathVariable Long userId){
        return userService.getUserById(userId);
    }

    @GetMapping(value = "users")
    public ResponseEntity getUsers(){
        return userService.getUsers();
    }

}
