package com.mita.controller;

import com.mita.dto.PublicUserDto;
import com.mita.dto.UserDto;
import com.mita.dto.request.user.AboutUpdateRequest;
import com.mita.dto.request.user.AvatarUpdateRequest;
import com.mita.dto.request.user.NameUpdateRequest;
import com.mita.dto.request.user.UserStatsDto;
import com.mita.service.FollowService;
import com.mita.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    public UserController(UserService userService, FollowService followService) {
        this.userService = userService;
        this.followService = followService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication));
    }

    @GetMapping("/me")
    public String me(){
        return userService.getCurrentUser().getUsername();
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

    @GetMapping("/{username}")
    public ResponseEntity<PublicUserDto> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsernamePublic(username));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PublicUserDto>> searchUsers(@RequestParam String username){
        return ResponseEntity.ok(userService.searchUsers(username));
    }


    @GetMapping("/{username}/followers")
    public Page<PublicUserDto> getFollowers(@PathVariable String username,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){
        return followService.getFollowers(username,page,size);
    }

    @GetMapping("/{username}/following")
    public Page<PublicUserDto> getFollowings(@PathVariable String username,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size){
        return followService.getFollowings(username,page,size);
    }

}
