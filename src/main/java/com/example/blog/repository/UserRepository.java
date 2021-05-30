package com.example.blog.repository;

import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    User getByUsernameAndPassword(String username, String password);

    List<User> findAllByPersonDeactivated(int i);

    List<User> findAllByPersonDeactivatedEquals(int number);
}
