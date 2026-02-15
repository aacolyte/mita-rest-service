package com.mita.service;

import com.mita.dto.UserDto;
import com.mita.entity.User;
import com.mita.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

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
                user.getUsernameField(),
                user.getAvatar(),
                user.getAbout()
        );
    }


    public UserDto updateAvatar(@RequestBody Map<String,String> body){
        User currentUser = getCurrentUser();
        String avatar = body.get("avatar");
        currentUser.setAvatar(avatar);
        userRepository.save(currentUser);
        return new UserDto(
                currentUser.getEmail(),
                currentUser.getUsernameField(),
                currentUser.getAvatar(),
                currentUser.getAbout()
        );
    }

    public UserDto updateAbout(@RequestBody Map<String,String> body){
        User currentUser = getCurrentUser();
        String about = body.get("about");
        currentUser.setAbout(about);
        userRepository.save(currentUser);
        return new UserDto(
                currentUser.getEmail(),
                currentUser.getUsernameField(),
                currentUser.getAvatar(),
                currentUser.getAbout()
        );
    }

    public UserDto updateName(@RequestBody Map<String,String> body){
        User currentUser = getCurrentUser();
        String name = body.get("name");
        currentUser.setUsername(name);
        userRepository.save(currentUser);
        return new UserDto(
                currentUser.getEmail(),
                currentUser.getUsernameField(),
                currentUser.getAvatar(),
                currentUser.getAbout()
        );
    }



}
