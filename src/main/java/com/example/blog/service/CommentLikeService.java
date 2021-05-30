package com.example.blog.service;

import com.example.blog.model.Comment;
import com.example.blog.model.CommentLike;
import com.example.blog.model.User;
import com.example.blog.repository.CommentLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CommentLikeService {
    @Autowired
    CommentLikeRepository commentLikeRepository;
    public void addCommentLike(CommentLike commentLike) {
        commentLikeRepository.save(commentLike);
    }

    public List<CommentLike> getAllByComment(Comment comment) {
        return commentLikeRepository.findAllByComment(comment);
    }

    public List<CommentLike> getAllByUser(User user) {
        return commentLikeRepository.findAllByUser(user);
    }

    public void deleteCommentLike(CommentLike commentLike) {
        commentLikeRepository.delete(commentLike);
    }

    public CommentLike getByCommentAndUser(Comment comment, User user) {
        return commentLikeRepository.findByCommentAndUser(comment, user);
    }
}
