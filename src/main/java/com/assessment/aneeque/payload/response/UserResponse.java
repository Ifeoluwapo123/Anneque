package com.assessment.aneeque.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserResponse {

    private Long id;

    private String username;

    private String gender;

    private String email;

}
