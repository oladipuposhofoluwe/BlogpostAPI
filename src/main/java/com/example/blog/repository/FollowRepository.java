package com.example.blog.repository;

import com.example.blog.model.Follow;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Follow findByFollowingAndUser(User followee, User follower);

    List<Follow> findAllByUser(User user);

    List<Follow> findAllByFollowing(User user);
}
