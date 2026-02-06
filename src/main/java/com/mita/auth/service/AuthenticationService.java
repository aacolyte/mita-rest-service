package com.mita.auth.service;

import com.mita.auth.dto.AuthenticationRequest;
import com.mita.auth.dto.AuthenticationResponse;
import com.mita.auth.dto.RegisterRequest;
import com.mita.entity.RefreshToken;
import com.mita.entity.Role;
import com.mita.entity.User;
import com.mita.repository.RefresherTokenRepository;
import com.mita.repository.UserRepository;
import com.mita.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final RefresherTokenRepository refresherTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationService(PasswordEncoder passwordEncoder, UserRepository repository, JwtService jwtService, AuthenticationManager authenticationManager, RefresherTokenRepository refresherTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.refresherTokenRepository = refresherTokenRepository;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User(
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                Role.USER
        );


        userRepository.save(user);

        return buildTokens(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        return buildTokens(user);
    }

    private AuthenticationResponse buildTokens(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(
                Date.from(LocalDateTime.now()
                        .plusDays(7)
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
        );
        refresherTokenRepository.save(refreshTokenEntity);

        return new AuthenticationResponse(accessToken,refreshToken);
    }

    public AuthenticationResponse refresh(String refreshToken) {

        return refresherTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    refresherTokenRepository.delete(token);
                    User user = token.getUser();
                    return buildTokens(user);
                })
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
    }


    public void deleteRefreshToken(String refreshToken) {
        refresherTokenRepository.findByToken(refreshToken).ifPresent(refresherTokenRepository::delete);
    }
}
