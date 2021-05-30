package com.example.blog.repository;

import com.example.blog.model.Favourite;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    List<Favourite> findAllByUser(User user);

    Favourite findByPost(Post post);

    Favourite findByPostAndUser(Post post, User user);
}
