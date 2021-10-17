package com.assessment.aneeque.controller;

import com.assessment.aneeque.Gender;
import com.assessment.aneeque.model.User;
import com.assessment.aneeque.payload.request.LoginRequest;
import com.assessment.aneeque.payload.request.RegistrationRequest;
import com.assessment.aneeque.repository.UserRepository;
import com.assessment.aneeque.security.jwt.AuthEntryPointJwt;
import com.assessment.aneeque.security.jwt.JwtUtils;
import com.assessment.aneeque.security.services.UserDetailsServiceImpl;
import com.assessment.aneeque.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(UserController.class)
@RunWith(SpringRunner.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private Authentication authentication;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    private HttpServletRequest request;

    @MockBean
    private HttpServletResponse response;

    @MockBean
    private AuthenticationException authException;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        this.mvc = webAppContextSetup(webApplicationContext).build();

        doNothing().when(authEntryPointJwt).commence(request, response, authException);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("a77hss44ns62993njd77ke.eje727722.jd99cd00cd");
        when(jwtUtils.getUserNameFromJwtToken(anyString())).thenReturn("controller test");
        when(jwtUtils.validateJwtToken("a77hss44ns62993njd77ke.eje727722.jd99cd00cd")).thenReturn(true);
    }

    @Test
    void shouldLoginUser() throws Exception {
        LoginRequest request = new LoginRequest();

        request.setPassword("uuuu");
        request.setEmail("adeyinkadewale@gmail.com");

        mvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(request))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRegisterUser() throws Exception {

        RegistrationRequest registrationRequest  = RegistrationRequest.builder()
                .username("Tes2ting")
                .email("test2@gmail.com")
                .gender("Gender.FEMALE.name()")
                .password("testing")
                .build();

        mvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(registrationRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUsers() throws Exception {
        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUserById() throws Exception {
        mvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}