package com.example.blog.repository;

import com.example.blog.model.Post;
import com.example.blog.model.PostLike;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    List<PostLike> findAllByPost(Post post);

    List<PostLike> findAllByUser(User user);

    PostLike findByPostAndUser(Post post, User user);
}
