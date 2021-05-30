package com.example.blog.controller;

import com.example.blog.model.*;
import com.example.blog.service.CommentLikeService;
import com.example.blog.service.CommentService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentLikeService commentLikeService;

    //Create new comment
    @PostMapping(path = "/{postId}/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createComment(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId, @Validated @RequestBody Comment comment) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Post> post1 = postService.getPostById(postId);
            if (post1.isPresent()) {
                comment.setUser(user.get());
                comment.setPost(post1.get());
                commentService.addComment(comment);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Get all comment for a post
    @GetMapping("/{postId}")
    public ResponseEntity<List<String>> getComments(@PathVariable(name = "postId") Long postId) {
        Optional<Post> post = postService.getPostById(postId);
        if (post.isPresent()) {
            Post post1 = post.get();
            List<Comment> comments = commentService.findAllByPost(post1);
            List<String> commentTexts = new ArrayList<>();
            for (Comment comment : comments)
                commentTexts.add(comment.getText());
            return new ResponseEntity<>(commentTexts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Get a comment for a post
    @GetMapping( "/{postId}/{commentId}")
    public ResponseEntity<Comment> getComment(@PathVariable(name = "commentId") Long commentId) {
        Optional<Comment> comment = commentService.getCommentById(commentId);
        return comment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    //Edit comment
    @PutMapping("/{userId}/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable(name = "userId") Long userId, @PathVariable(name = "commentId") Long commentId, @Validated @RequestBody Comment updateComment) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Comment> comment = commentService.getCommentById(commentId);
            if (comment.isPresent()) {
                Comment comment1 = comment.get();
                if (comment1.getUser().getId().equals(userId)){
                    comment1.setText(updateComment.getText());
                    commentService.addComment(comment1);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Delete comment
    @DeleteMapping("/{userId}/{commentId}")
    public ResponseEntity<Post> deleteComment(@PathVariable(name = "userId") Long userId, @PathVariable(name = "commentId") Long commentId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Comment> comment = commentService.getCommentById(commentId);
            if (comment.isPresent()) {
                Comment comment1 = comment.get();
                if (comment1.getUser().getId().equals(userId)){
                    commentService.deleteComment(comment1);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    //Like a post
    @PostMapping(path = "/{commentId}/like/{userId}")
    public ResponseEntity<?> likeComment(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Comment> comment = commentService.getCommentById(commentId);
        if (user.isPresent()) {
            if (comment.isPresent()) {
                CommentLike like = commentLikeService.getByCommentAndUser(comment.get(), user.get());
                if (like == null) {
                    CommentLike commentLike = new CommentLike();
                    commentLike.setComment(comment.get());
                    commentLike.setUser(user.get());
                    commentLikeService.addCommentLike(commentLike);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    commentLikeService.deleteCommentLike(like);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get number of likes for a comment
    @GetMapping( "/{commentId}/likes")
    public ResponseEntity<?> getCommentLikes(@PathVariable(name = "commentId") Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            int size = commentLikeService.getAllByComment(commentService.getCommentById(id).get()).size();
            return new ResponseEntity<>(size, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
