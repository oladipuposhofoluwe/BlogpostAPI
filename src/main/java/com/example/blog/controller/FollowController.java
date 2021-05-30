package com.example.blog.controller;

import com.example.blog.model.Follow;
import com.example.blog.model.User;
import com.example.blog.service.FollowService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/follows")
public class FollowController {
    @Autowired
    UserService userService;

    @Autowired
    FollowService followService;

    // create connection
    @PostMapping("/{followingId}/{userId}")
    public ResponseEntity<?> follow(@PathVariable(name = "followingId") Long followingId, @PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<User> followee = userService.getUserById(followingId);
            if (followee.isPresent()) {
                Follow follow = followService.getByFollowingAndUser(followee.get(), user.get());
                if (follow == null) {
                    Follow newFollow = new Follow();
                    newFollow.setFollowing(followee.get());
                    newFollow.setUser(user.get());
                    followService.addFollow(newFollow);
                    return new ResponseEntity<>("Users Connection Successful", HttpStatus.OK);
                }

            }
        }
            return new ResponseEntity<>("Not A Registered User", HttpStatus.BAD_REQUEST);
    }

    //Get a user following
    @GetMapping("/{userId}/followings")
    public ResponseEntity<?> following(@PathVariable(name = "userId") Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            List<Follow> follows =  followService.getAllByUser(user.get());
            List<User> followers = new ArrayList<>();
            for (Follow follow: follows)
                followers.add(follow.getFollowing());
            return new ResponseEntity<>(followers, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not a registered user", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<?> follower(@PathVariable(name = "userId") Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            List<Follow> follows =  followService.getAllByFollowing(user.get());
            List<User> followings = new ArrayList<>();
            for (Follow follow: follows)
                followings.add(follow.getUser());
            return new ResponseEntity<>(followings, HttpStatus.OK);
        }
        return new ResponseEntity<>("Not a Registered User", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{followingId}/{userId}")
    public ResponseEntity<?> unfollow(@PathVariable(name = "followingId") Long followeeId, @PathVariable(name = "userId") Long followerId) {
        Optional<User> follower = userService.getUserById(followerId);
        if (follower.isPresent()) {
            Optional<User> followee = userService.getUserById(followeeId);
            if (followee.isPresent()) {
                Follow follow = followService.getByFollowingAndUser(followee.get(), follower.get());
                if (follow != null)
                followService.deleteFollow(follow);
                return new ResponseEntity<>("Users Disconnection Successfully",HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Not a Register User", HttpStatus.BAD_REQUEST);
    }
}
