package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.repository.UserRepository;
import com.mita.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;
    private final UserRepository userRepository;

    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user-by-email")
    public UserDto getUserByEmail(@RequestParam Map<String, String> body){
        return userService.getUserByEmail(body);
    }

    @DeleteMapping("/delete-user-by-email")
    public void deleteUserByEmail(@RequestParam Map<String, String> body){
        userService.deleteUserByEmail(body);
    }


}
