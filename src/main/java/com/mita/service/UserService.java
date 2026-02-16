package com.mita.service;

import com.mita.dto.UserDto;
import com.mita.dto.request.UserStatsDto;
import com.mita.entity.User;
import com.mita.repository.CategoryRepository;
import com.mita.repository.ItemRepository;
import com.mita.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Transactional
public class UserService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, ItemRepository itemRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
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


    public UserDto updateAvatar(Map<String,String> body){
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

    public UserDto updateAbout(Map<String,String> body){
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

    public UserDto updateName(Map<String,String> body){
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


    public UserStatsDto getUserStats() {
        User currentUser = getCurrentUser();
        int totalItems = itemRepository.countByUserId(currentUser.getId());
        int totalCategories = categoryRepository.countByUserId(currentUser.getId());

        return new UserStatsDto(
                totalItems,
                totalCategories
        );
    }

    public UserDto getUserByEmail(Map<String,String> body) {
        String email = body.get("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));

        return new UserDto(
                user.getEmail(),
                user.getUsernameField(),
                user.getAvatar(),
                user.getAbout()
        );
    }

    public void deleteUserByEmail(Map<String, String> body) {
        String email = body.get("email");
        userRepository.deleteByEmail(email);
    }



}
