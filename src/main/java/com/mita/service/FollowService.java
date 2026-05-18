package com.mita.service;

import com.mita.entity.Follow;
import com.mita.entity.User;
import com.mita.repository.FollowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
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



}
