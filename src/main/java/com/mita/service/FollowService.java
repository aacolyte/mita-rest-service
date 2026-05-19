package com.mita.service;

import com.mita.dto.PublicUserDto;
import com.mita.entity.Follow;
import com.mita.entity.User;
import com.mita.repository.FollowRepository;
import com.mita.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public FollowService(FollowRepository followRepository, UserService userService, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public void follow(String username){
        User current = userService.getCurrentUser();
        User target = userService.getUserByUsernamePrivate(username);

        if(current.getId().equals(target.getId())){
            return;
        }

        if(!followRepository.existsByFollowerIdAndFollowingId(
                current.getId(), target.getId()
        )){
            followRepository.save(new Follow(current.getId(), target.getId()));
        }
    }

    @Transactional
    public void unfollow(String username){
        User current = userService.getCurrentUser();
        User target = userService.getUserByUsernamePrivate(username);

        followRepository.deleteByFollowerIdAndFollowingId(
                current.getId(), target.getId()
        );
    }

    public boolean isFollowing(String username){
        User current = userService.getCurrentUser();
        User target = userService.getUserByUsernamePrivate(username);

        return followRepository.existsByFollowerIdAndFollowingId(
                current.getId(), target.getId()
        );
    }


    public Page<PublicUserDto> getFollowers(String username, int page, int size){
        User user = userService.getUserByUsernamePrivate(username);

        return followRepository
                .findByFollowingId(user.getId(), PageRequest.of(page, size))
                .map(follow -> {
                    User follower = userRepository.findById(follow.getFollowerId()).orElseThrow();
                    return new PublicUserDto(
                            follower.getUsernameField(),
                            follower.getAvatar(),
                            follower.getAbout(),
                            0,
                            0
                    );
                });
    }

    public Page<PublicUserDto> getFollowings(String username, int page, int size){
        User user = userService.getUserByUsernamePrivate(username);

        return followRepository
                .findByFollowerId(user.getId(), PageRequest.of(page,size))
                .map(follow -> {
                    User following = userRepository.findById(follow.getFollowingId()).orElseThrow();
                    return new PublicUserDto(
                            following.getUsernameField(),
                            following.getAvatar(),
                            following.getAbout(),
                            0,
                            0
                    );
                });
    }


}
