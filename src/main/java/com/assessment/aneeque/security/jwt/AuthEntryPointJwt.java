package com.assessment.aneeque.security.jwt;

import com.assessment.aneeque.payload.response.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthEntryPointJwt implements
        AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Responding with unauthorized error. Message - {}", authException.getMessage());

        if(authException instanceof BadCredentialsException) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            response.getOutputStream().write(new ObjectMapper()
                    .writeValueAsBytes(new MessageResponse("Invalid username and password")));
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Unauthorized access");
        }
    }

}
