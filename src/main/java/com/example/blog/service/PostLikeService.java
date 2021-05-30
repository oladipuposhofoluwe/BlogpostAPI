package com.example.blog.service;

import com.example.blog.model.Post;
import com.example.blog.model.PostLike;
import com.example.blog.model.User;
import com.example.blog.repository.PostLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostLikeService {
    @Autowired
    PostLikeRepository postLikeRepository;

    public void addPostLike(PostLike postLike) {
        postLikeRepository.save(postLike);
    }

    public List<PostLike> getAllByPost(Post post) {
        return postLikeRepository.findAllByPost(post);
    }

    public List<PostLike> getAllByUser(User user) {
        return postLikeRepository.findAllByUser(user);
    }

    public void deletePostLike(PostLike postLike) {
        postLikeRepository.delete(postLike);
    }

    public PostLike getByPostAndUser(Post post, User user) {
        return postLikeRepository.findByPostAndUser(post, user);
    }
}
