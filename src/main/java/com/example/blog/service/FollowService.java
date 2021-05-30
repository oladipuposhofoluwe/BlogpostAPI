package com.example.blog.service;

import com.example.blog.model.Follow;
import com.example.blog.model.User;
import com.example.blog.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;


    public void deleteFollow(Follow follow) {
        followRepository.delete(follow);
    }

    public void addFollow(Follow newFollow) {
        followRepository.save(newFollow);
    }

    public List<Follow> getAllByUser(User user) {
        return followRepository.findAllByUser(user);
    }

    public List<Follow> getAllByFollowing(User user) {
        return followRepository.findAllByFollowing(user);
    }

    public Follow getByFollowingAndUser(User user, User user1) {
        return followRepository.findByFollowingAndUser(user, user1);
    }
}
