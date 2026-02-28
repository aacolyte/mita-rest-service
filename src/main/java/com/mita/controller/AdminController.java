package com.mita.controller;

import com.mita.dto.UserDto;
import com.mita.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-by-email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @DeleteMapping("/delete-user-by-email")
    public ResponseEntity<Void> deleteUserByEmail(@RequestParam String email){
        userService.deleteUserByEmail(email);

        return ResponseEntity.noContent().build();
    }


}
