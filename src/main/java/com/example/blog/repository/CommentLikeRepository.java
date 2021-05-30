package com.example.blog.repository;

import com.example.blog.model.Comment;
import com.example.blog.model.CommentLike;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    List<CommentLike> findAllByComment(Comment comment);

    List<CommentLike> findAllByUser(User user);

    CommentLike findByCommentAndUser(Comment comment, User user);
}
