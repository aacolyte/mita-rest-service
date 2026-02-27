package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.dto.request.user.AboutUpdateRequest;
import com.mita.dto.request.user.AvatarUpdateRequest;
import com.mita.dto.request.user.NameUpdateRequest;
import com.mita.dto.request.user.UserStatsDto;
import com.mita.repository.UserRepository;
import com.mita.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    public UserDto updateAvatar(@RequestBody AvatarUpdateRequest request){
        return userService.updateAvatar(request);
    }


    @PutMapping("/about")
    public UserDto updateAbout(@RequestBody AboutUpdateRequest request){
        return userService.updateAbout(request);
    }

    @PutMapping("/name")
    public UserDto updateName(@RequestBody NameUpdateRequest request){
        return userService.updateName(request);
    }

    @GetMapping("/stats")
    public UserStatsDto getUserStats(){
        return userService.getUserStats();
    }

}
