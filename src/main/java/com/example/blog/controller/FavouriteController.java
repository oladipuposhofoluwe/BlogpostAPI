package com.example.blog.controller;

import com.example.blog.model.Favourite;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.service.FavouriteService;
import com.example.blog.service.PostService;
import com.example.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stars")
public class FavouriteController {
    @Autowired
    PostService postService;

    @Autowired
    FavouriteService favouriteService;

    @Autowired
    UserService userService;

    //Star a post or add to favourite list
    @PostMapping("/{postId}/{userId}")
    public ResponseEntity<?> starPost(@PathVariable(name = "postId") Long postId, @PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Post> post1 = postService.getPostById(postId);
            if (post1.isPresent()) {
                Favourite favourite = favouriteService.getByPostAndUser(post1.get(), user.get());
                if (favourite == null) {
                    Favourite fav = new Favourite();
                    fav.setUser(user.get());
                    fav.setPost(post1.get());
                    favouriteService.addComment(fav);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    //get all starred post by user
    @GetMapping("/{userId}")
    public ResponseEntity<List> getStarPosts(@PathVariable(name = "userId") Long userId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            List<Favourite> favourites = favouriteService.findAllByUser(user.get());
            List<Post> posts = new ArrayList<>();
            for (Favourite favourite : favourites)
                posts.add(favourite.getPost());
            return new ResponseEntity<List>(posts,  HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // delete post from favourite list
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<?> deleteStarredPost (@PathVariable(name = "userId") Long userId, @PathVariable(name = "postId") Long postId) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            Optional<Post> post = postService.getPostById(postId);
            if (post.isPresent()) {
                Favourite favourite = favouriteService.getByPostAndUser(post.get(), user.get());
                if (favourite != null) {
                    favouriteService.deleteFavourite(favourite);
                    return new ResponseEntity<>(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
