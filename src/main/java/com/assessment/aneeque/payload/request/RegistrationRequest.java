package com.assessment.aneeque.payload.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RegistrationRequest {

    @NotNull(message = "{name.null}")
    @NotBlank(message = "{name.empty}")
    private String username;

    @NotNull(message = "{gender.null}")
    @NotBlank(message = "{gender.empty}")
    private String gender;

    @NotNull(message = "{emailNull}")
    @Email(message = "{email.invalid}")
    @NotBlank(message = "{email.empty}")
    private String email;

    @NotNull(message = "{password.null}")
    @NotBlank(message = "{password.empty}")
    private String password;
}
