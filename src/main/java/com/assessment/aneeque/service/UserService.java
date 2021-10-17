package com.assessment.aneeque.service;

import com.assessment.aneeque.payload.request.LoginRequest;
import com.assessment.aneeque.payload.request.RegistrationRequest;
import com.assessment.aneeque.payload.response.ListOfUsersResponse;
import com.assessment.aneeque.payload.response.LoginResponse;
import com.assessment.aneeque.payload.response.MessageResponse;
import com.assessment.aneeque.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<MessageResponse> registerUser(RegistrationRequest registrationRequest);
    ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest);
    ResponseEntity<UserResponse> getUserById(Long userId);
    ResponseEntity<ListOfUsersResponse> getUsers();
}
