package dev.oguzhanercelik.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "loginRequest.email.notBlank")
    @Email(message = "loginRequest.email.pattern")
    private String email;
    @NotBlank(message = "loginRequest.password.notBlank")
    private String password;

}