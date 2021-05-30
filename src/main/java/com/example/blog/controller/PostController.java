package com.example.blog.controller;

import com.example.blog.model.Post;
import com.example.blog.model.PostLike;
import com.example.blog.model.User;
import com.example.blog.service.PostLikeService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/posts")
public class  PostController {
    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    PostLikeService postLikeService;


    //Create new post
    @PostMapping(path = "/{userid}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createPost(@PathVariable(name = "userid") Long id, @Validated @RequestBody Post post) {
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            post.setUser(user.get());
            postService.addPost(post);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    //Get All post
    @GetMapping
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //Get all post by user
    @GetMapping("/{userId}/posts")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            List<Post> posts = postService.findAllByUser(user.get());
            return new ResponseEntity<>(posts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    //Get post by post id
    @GetMapping( "/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable(name = "postId") Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }


    //Edit post
    @PutMapping("/{userId}/{postId}")
    public ResponseEntity<Post> updatePost(@PathVariable(name = "userId") Long userId, @PathVariable(name = "postId") Long postId, @Validated @RequestBody Post updatePost) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                Post post1 = post.get();
                if (post1.getUser().getId().equals(userId)){
                    post1.setText(updatePost.getText());
                    postService.addPost(post1);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    //Delete Post
    @DeleteMapping("/{userId}/{postId}")
    public ResponseEntity<Post> deletePost(@PathVariable(name = "userId") Long userId, @PathVariable(name = "postId") Long postId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                Post post1 = post.get();
                if (post1.getUser().getId().equals(userId)){
                    postService.deletePost(post1);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //Like a post
    @PostMapping(path = "/{postId}/like/{userId}")
    public ResponseEntity<?> likeOrDislikePost(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Post> post = postService.getPostById(postId);
        if (user.isPresent()) {
            if (post.isPresent()) {
                PostLike like = postLikeService.getByPostAndUser(post.get(), user.get());
                if (like == null) {
                    PostLike postLike = new PostLike();
                    postLike.setPost(post.get());
                    postLike.setUser(user.get());
                    postLikeService.addPostLike(postLike);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    postLikeService.deletePostLike(like);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // Get number of likes for a post
    @GetMapping( "/{postId}/likes")
    public ResponseEntity<?> getPostLikes(@PathVariable(name = "postId") Long id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            int size = postLikeService.getAllByPost(postService.getPostById(id).get()).size();
            return new ResponseEntity<>(size, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
