package com.assessment.aneeque.service.impl;

import com.assessment.aneeque.exception.ApiBadRequestException;
import com.assessment.aneeque.exception.ApiConflictException;
import com.assessment.aneeque.exception.ApiResourceNotFoundException;
import com.assessment.aneeque.model.User;
import com.assessment.aneeque.payload.request.LoginRequest;
import com.assessment.aneeque.payload.request.RegistrationRequest;
import com.assessment.aneeque.payload.response.ListOfUsersResponse;
import com.assessment.aneeque.payload.response.LoginResponse;
import com.assessment.aneeque.payload.response.MessageResponse;
import com.assessment.aneeque.payload.response.UserResponse;
import com.assessment.aneeque.repository.UserRepository;
import com.assessment.aneeque.security.jwt.JwtUtils;
import com.assessment.aneeque.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<MessageResponse> registerUser(RegistrationRequest registrationRequest){
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail());

        if(optionalUser.isPresent()) throw new ApiConflictException("User already exists");

        saveUser(User.builder()
                .email(registrationRequest.getEmail())
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .gender(registrationRequest.getGender())
                .build());


        return new ResponseEntity<>(new MessageResponse("registration successful"), HttpStatus.CREATED);
    }

    public ResponseEntity<LoginResponse> loginUser(LoginRequest loginRequest){

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                ()-> new ApiResourceNotFoundException("Email does not exist")
        );

        System.out.println(user.getPassword());
        System.out.println(passwordEncoder.encode(loginRequest.getPassword()));
        System.out.println(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()));

        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new ApiBadRequestException("Invalid Password");
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new ResponseEntity<>(LoginResponse.builder().token(jwtUtils.generateJwtToken(authentication)).message("logged-in successfully").build(), HttpStatus.OK);
    }

    public ResponseEntity<UserResponse> getUserById(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new ApiResourceNotFoundException("User not found"));
        return new ResponseEntity<>(modelMapper.map(user, UserResponse.class), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<ListOfUsersResponse> getUsers(){
        return new ResponseEntity<>(new ListOfUsersResponse(userRepository.findAll()), HttpStatus.ACCEPTED);
    }

    private void saveUser(User user){
        try{
            userRepository.save(user);
        }catch (DataAccessException exception){
            logger.error(exception.getMessage());
        }
    }
}
