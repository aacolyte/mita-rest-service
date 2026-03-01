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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    private final Logger log = LoggerFactory.getLogger(AuthenticationService.class);


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
        log.info("User {} registered", user.getEmail());


        return buildTokens(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("User {} logged in", user.getEmail());

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
                        .plusDays(30)
                        .atZone(ZoneId.systemDefault())
                        .toInstant())
        );
        refresherTokenRepository.save(refreshTokenEntity);

        return new AuthenticationResponse(accessToken,refreshToken);
    }

    @Transactional
    public AuthenticationResponse refresh(String refreshToken) {

        RefreshToken tokenEntity = refresherTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired refresh token"));

        String email = jwtService.getEmailFromToken(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        refresherTokenRepository.delete(tokenEntity);

        log.info("Refresh for user {}", user.getEmail());

        return buildTokens(user);
    }


    public void deleteRefreshToken(String refreshToken) {
        refresherTokenRepository.findByToken(refreshToken).ifPresent(token -> {
            refresherTokenRepository.delete(token);
            log.info("Deleted refresh token for user {}", token.getUser().getEmail());
        });
    }
}
