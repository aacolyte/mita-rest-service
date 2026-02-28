package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.dto.request.user.AboutUpdateRequest;
import com.mita.dto.request.user.AvatarUpdateRequest;
import com.mita.dto.request.user.NameUpdateRequest;
import com.mita.dto.request.user.UserStatsDto;
import com.mita.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication));
    }

    @PutMapping("/avatar")
    public ResponseEntity<UserDto> updateAvatar(@RequestBody AvatarUpdateRequest request){
        return ResponseEntity.ok(userService.updateAvatar(request));
    }


    @PutMapping("/about")
    public ResponseEntity<UserDto> updateAbout(@RequestBody AboutUpdateRequest request){
        return ResponseEntity.ok(userService.updateAbout(request));
    }

    @PutMapping("/name")
    public ResponseEntity<UserDto> updateName(@RequestBody NameUpdateRequest request){
        return ResponseEntity.ok(userService.updateName(request));
    }

    @GetMapping("/stats")
    public ResponseEntity<UserStatsDto> getUserStats(){
        return ResponseEntity.ok(userService.getUserStats());
    }

}
