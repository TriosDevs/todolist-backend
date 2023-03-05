package dev.oguzhanercelik.service;

import dev.oguzhanercelik.config.security.PasswordEncoder;
import dev.oguzhanercelik.converter.IdentityUserConverter;
import dev.oguzhanercelik.converter.UserConverter;
import dev.oguzhanercelik.entity.User;
import dev.oguzhanercelik.exception.ApiException;
import dev.oguzhanercelik.model.UserAuthentication;
import dev.oguzhanercelik.model.error.ErrorEnum;
import dev.oguzhanercelik.model.request.LoginRequest;
import dev.oguzhanercelik.model.request.RegisterRequest;
import dev.oguzhanercelik.model.response.AuthenticationResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final TokenService tokenService;
    private final IdentityUserConverter identityUserConverter;
    private final UserConverter userConverter;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse login(LoginRequest request) throws Exception {
//        request.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(request.getPassword()));
//        final Optional<User> optional = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
//        if (optional.isEmpty()) {
//            throw new ApiException(ErrorEnum.UNAUTHORIZED);
//        }
//        final User user = optional.get();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("DisabledException");
        } catch (BadCredentialsException e) {
            throw new Exception("BadCredentialsException");
        }

        Optional<User> user = userService.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            throw new ApiException(ErrorEnum.UNAUTHORIZED);
        }



        System.out.println("user = " + user.get().getEmail());

        // check if user is active

        final String token = tokenService.createToken(userConverter.toDto(user.get()));
        return new AuthenticationResponse(token);
    }

    public Authentication getUserAuthentication(HttpServletRequest httpServletRequest) {
        final Claims claims = tokenService.getTokenClaims(httpServletRequest);
        return new UserAuthentication(identityUserConverter.getUser(claims));
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        userService.checkEmailIfExist(registerRequest.getEmail());
        registerRequest.setPassword(passwordEncoder.bCryptPasswordEncoder().encode(registerRequest.getPassword()));
        final User user = userConverter.toEntity(registerRequest);
        userService.save(user);

        // send email to user

    }

}