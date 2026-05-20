package com.mita.service;

import com.mita.dto.PublicUserDto;
import com.mita.dto.UserDto;
import com.mita.dto.request.user.AboutUpdateRequest;
import com.mita.dto.request.user.AvatarUpdateRequest;
import com.mita.dto.request.user.NameUpdateRequest;
import com.mita.dto.request.user.UserStatsDto;
import com.mita.entity.User;
import com.mita.exception.FieldConflictException;
import com.mita.repository.CategoryRepository;
import com.mita.repository.FollowRepository;
import com.mita.repository.ItemRepository;
import com.mita.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final UploadService uploadService;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final FollowRepository followRepository;

    public UserService(UserRepository userRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, UploadService uploadService, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.uploadService = uploadService;
        this.followRepository = followRepository;
    }

    public User getCurrentUser(){
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        Object principal = auth.getPrincipal();
        if(principal instanceof User user){
            return user;
        }else{
            throw new IllegalStateException("User is not authenticated");
        }
    }

    @Transactional(readOnly = true)
    public UserDto getProfile(Authentication authentication){
        String username = authentication.getName();

        User user = userRepository
                .findByEmail(username)
                .orElseThrow();

        return user.toDto();
    }


    public UserDto updateAvatar(AvatarUpdateRequest request){
        User currentUser = getCurrentUser();

        String oldAvatar = currentUser.getAvatar();

        String avatar = request.getAvatar();
        currentUser.setAvatar(avatar);
        userRepository.save(currentUser);

        if(oldAvatar != null && !oldAvatar.equals(avatar)){
            uploadService.deletePosterIfExists(oldAvatar);
        }

        log.info("User {} updated avatar", currentUser.getId());

        return currentUser.toDto();
    }

    public UserDto updateAbout(AboutUpdateRequest request){
        User currentUser = getCurrentUser();
        String about = request.getAbout();
        currentUser.setAbout(about);
        userRepository.save(currentUser);

        log.info("User {} updated about", currentUser.getId());

        return currentUser.toDto();
    }

    public UserDto updateName(NameUpdateRequest request){
        User currentUser = getCurrentUser();
        String name = request.getName();

        if(userRepository.existsByUsername(name)){
            throw new FieldConflictException("USERNAME_TAKEN");
        }

        currentUser.setUsername(name);
        userRepository.save(currentUser);

        log.info("User {} updated name", currentUser.getId());

        return currentUser.toDto();
    }

    @Transactional(readOnly = true)
    public UserStatsDto getUserStats() {
        User currentUser = getCurrentUser();
        int totalItems = itemRepository.countByUserId(currentUser.getId());
        int totalCategories = categoryRepository.countByUserId(currentUser.getId());

        return new UserStatsDto(
                totalItems,
                totalCategories
        );
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User with email " + email + " not found"));

        return user.toDto();
    }


    @Transactional(readOnly = true)
    public PublicUserDto getUserByUsernamePublic(String username){
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));

        long followers = followRepository.countByFollowing_Id(user.getId());
        long followings = followRepository.countByFollower_Id(user.getId());

        return new PublicUserDto(
                user.getUsernameField(),
                user.getAvatar(),
                user.getAbout(),
                followers,
                followings
        );
    }

    @Transactional(readOnly = true)
    public User getUserByUsernamePrivate(String username){
        User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));

        return user;
    }

    public void deleteUserByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public List<PublicUserDto> searchUsers(String name){
        return userRepository.findTop10ByUsernameContainingIgnoreCase(name)
                .stream()
                .map(user -> new PublicUserDto(
                        user.getUsernameField(),
                        user.getAvatar(),
                        user.getAbout(),
                        0,
                        0
                ))
                .toList();
    }


}
