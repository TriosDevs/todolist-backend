package dev.oguzhanercelik.controller;

import dev.oguzhanercelik.config.MdcConstant;
import dev.oguzhanercelik.model.dto.UserDto;
import dev.oguzhanercelik.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public UserDto getProfile() {
        return userService.getUserInfo(Integer.valueOf(MDC.get(MdcConstant.X_USER_ID)));
    }

}
