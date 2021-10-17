package com.assessment.aneeque.service.impl;

import com.assessment.aneeque.Gender;
import com.assessment.aneeque.exception.ApiBadRequestException;
import com.assessment.aneeque.exception.ApiConflictException;
import com.assessment.aneeque.exception.ApiResourceNotFoundException;
import com.assessment.aneeque.model.User;
import com.assessment.aneeque.payload.request.LoginRequest;
import com.assessment.aneeque.payload.request.RegistrationRequest;
import com.assessment.aneeque.payload.response.UserResponse;
import com.assessment.aneeque.repository.UserRepository;
import com.assessment.aneeque.security.jwt.JwtUtils;
import com.assessment.aneeque.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith({MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private ModelMapper modelMapper;

    private UserService userService;

    private RegistrationRequest registrationRequest;

    private LoginRequest loginRequest;

    private User user;

    @BeforeEach
    void setUp() {
        registrationRequest = RegistrationRequest.builder()
                .username("Testing")
                .email("test@gmail.com")
                .gender(Gender.FEMALE.name())
                .password("testing")
                .build();

        loginRequest = LoginRequest.builder()
                .email("test@gmail.com")
                .password("testing")
                .build();

        user = User.builder()
                .username(registrationRequest.getUsername())
                .email(registrationRequest.getEmail())
                .gender(registrationRequest.getGender())
                .password(registrationRequest.getPassword())
                .build();

        userService = new UserServiceImpl(userRepository, passwordEncoder, authenticationManager, jwtUtils, modelMapper);
    }

    @Test
    void ShouldFailToRegisterUser(){
        when(userRepository.findByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail()))
                .thenReturn(Optional.ofNullable(user));

        assertThatThrownBy(()-> userService.registerUser(registrationRequest))
                .isInstanceOf(ApiConflictException.class)
                .hasMessageContaining("User already exists");
    }

    @Test
    void ShouldPassToRegisterUser(){
        when(userRepository.findByUsernameOrEmail(registrationRequest.getUsername(), registrationRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThat(userService.registerUser(registrationRequest).getStatusCodeValue()).isEqualTo(201);
        assertThat(userService.registerUser(registrationRequest).getBody().getMessage()).isEqualTo("registration successful");
        assertThat(userService.registerUser(registrationRequest).getBody()).isNotNull();
    }

    @Test
    void ShouldFailToLoginEmailDoesNotExist(){
        when(userRepository.findByEmail(registrationRequest.getEmail()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> userService.loginUser(loginRequest))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessageContaining("Email does not exist");
    }

    @Test
    void ShouldFailToLoginPasswordDoesNotMatch(){
        when(userRepository.findByEmail(registrationRequest.getEmail()))
                .thenReturn(Optional.ofNullable(user));

        assertThatThrownBy(()-> userService.loginUser(loginRequest))
                .isInstanceOf(ApiBadRequestException.class)
                .hasMessageContaining("Invalid Password");
    }

    @Test
    void ShouldLoginSuccessfully(){
        when(userRepository.findByEmail(registrationRequest.getEmail()))
                .thenReturn(Optional.ofNullable(user));

        given(passwordEncoder.matches(registrationRequest.getPassword(), "testing"))
                .willReturn(true);

        assertThat(userService.loginUser(loginRequest).getStatusCodeValue()).isEqualTo(200);
        assertThat(userService.loginUser(loginRequest).getBody().getMessage()).isEqualTo("logged-in successfully");
        assertThat(userService.loginUser(loginRequest).getBody()).isNotNull();
    }

    @Test
    void ShouldGetUserById(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.ofNullable(user));

        when(modelMapper.map(user, UserResponse.class)).thenReturn(UserResponse.builder()
                .username(registrationRequest.getUsername())
                .gender(registrationRequest.getGender())
                .email(registrationRequest.getEmail())
                .id(1L)
                .build());

        assertThat(userService.getUserById(1L).getStatusCodeValue()).isEqualTo(202);
        assertThat(userService.getUserById(1L).getBody()).isNotNull();
        assertThat(userService.getUserById(1L).getBody().getUsername()).isEqualTo("Testing");
        assertThat(userService.getUserById(1L).getBody().getGender()).isEqualTo(Gender.FEMALE.name());
        assertThat(userService.getUserById(1L).getBody().getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void ShouldFailToGetUserById(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()-> userService.getUserById(1L))
                .isInstanceOf(ApiResourceNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void ShouldGetAllUsers(){
        when(userRepository.findAll())
                .thenReturn(new ArrayList<User>());

        assertThat(userService.getUsers().getStatusCodeValue()).isEqualTo(202);
    }
}