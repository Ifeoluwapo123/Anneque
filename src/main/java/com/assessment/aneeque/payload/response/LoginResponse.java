package com.assessment.aneeque.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginResponse {
    private String message;
    private String token;
}
