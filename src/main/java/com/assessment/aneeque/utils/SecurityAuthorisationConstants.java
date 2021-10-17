package com.assessment.aneeque.utils;

public class SecurityAuthorisationConstants {
    public static final String[] PUBLIC_URIS = new String[]{
            "/",
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui/#/**",
            "/api/user/login"
    };
}