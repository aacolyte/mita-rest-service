package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.entity.User;
import com.mita.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public UserDto getProfile(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository
                .findByEmail(username)
                .orElseThrow();

        return new UserDto(
                user.getEmail(),
                user.getUsernameField()
        );

    }


}
