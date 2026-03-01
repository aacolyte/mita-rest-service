package com.mita.auth.controller;

import com.mita.auth.dto.AuthenticationRequest;
import com.mita.auth.dto.AuthenticationResponse;
import com.mita.auth.dto.RegisterRequest;
import com.mita.auth.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>  authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refresh(
            @RequestBody Map<String, String> request
    ){
        String refreshToken = request.get("refreshToken");

        return authenticationService.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken) {

        if(refreshToken != null) {
            authenticationService.deleteRefreshToken(refreshToken);
        }
        return ResponseEntity.ok("Successfully logged out");
    }

}
