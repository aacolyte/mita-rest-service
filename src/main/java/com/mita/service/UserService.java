package com.mita.service;

import com.mita.dto.UserDto;
import com.mita.entity.User;
import com.mita.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(){
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    public UserDto getProfile(Authentication authentication){
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
