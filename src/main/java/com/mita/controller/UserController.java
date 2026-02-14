package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.repository.UserRepository;
import com.mita.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public UserDto getProfile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PutMapping("/avatar")
    public UserDto updateAvatar(@RequestBody Map<String,String> body){
        return userService.updateAvatar(body);
    }


    @PutMapping("/about")
    public UserDto updateAbout(@RequestBody Map<String,String> body){
        return userService.updateAbout(body);
    }

}
