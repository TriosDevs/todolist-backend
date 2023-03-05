package dev.oguzhanercelik.controller;

import dev.oguzhanercelik.model.request.LoginRequest;
import dev.oguzhanercelik.model.request.RegisterRequest;
import dev.oguzhanercelik.model.response.AuthenticationResponse;
import dev.oguzhanercelik.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) throws Exception {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authenticationService.register(registerRequest);
    }

}
