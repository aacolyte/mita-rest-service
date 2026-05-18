package com.mita.controller;

import com.mita.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }


    @PostMapping("/{username}/follow")
    public ResponseEntity<Void> follow(@PathVariable String username){
        followService.follow(username);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<Void> unfollow(@PathVariable String username){
        followService.unfollow(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{username}/follow-status")
    public ResponseEntity<Boolean> isFollowing(@PathVariable String username){
        Boolean isFollowing = followService.isFollowing(username);
        return ResponseEntity.ok(isFollowing);
    }


}
