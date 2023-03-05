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
public class RegisterRequest {

    @NotBlank(message = "registerRequest.firstName.notBlank")
    private String firstName;
    @NotBlank(message = "registerRequest.lastName.notBlank")
    private String lastName;
    @NotBlank(message = "registerRequest.email.notBlank")
    @Email(message = "registerRequest.email.pattern")
    private String email;
    @NotBlank(message = "registerRequest.password.notBlank")
    private String password;

}
