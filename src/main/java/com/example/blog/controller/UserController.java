package com.example.blog.controller;

import com.example.blog.model.User;
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
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user) {
        User user1 = userService.getUserByUsername(user.getUsername());
        if (user1 == null) {
            userService.addUser(user);
            return new ResponseEntity<>("User created", HttpStatus.OK);
        }
        return new ResponseEntity<>("User with this username already exists", HttpStatus.BAD_REQUEST);
    }



    @GetMapping( "/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.findAll(0);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id){
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty()) {
            return new ResponseEntity<>("You are not a registered user",HttpStatus.BAD_REQUEST);
        }
        userService.deleteUser(user);
        return new ResponseEntity<>("request pending", HttpStatus.NO_CONTENT);

    }

    @PostMapping("/reactivate/{id}")
    public ResponseEntity<?> reverseAccountDeletion(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        if(user.isEmpty())
            return new ResponseEntity<>("Not A registered User", HttpStatus.UNAUTHORIZED);
        String message = userService.reverseDelete(user.get());
        if(message.equals("Delete action successfully reversed"))
            return new ResponseEntity<>("successfully reversed", HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>("user not authorized", HttpStatus.UNAUTHORIZED);
    }

}
