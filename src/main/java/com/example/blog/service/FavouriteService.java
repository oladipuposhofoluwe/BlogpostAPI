package com.example.blog.service;

import com.example.blog.model.Favourite;
import com.example.blog.model.Post;
import com.example.blog.model.User;
import com.example.blog.repository.FavouriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Access;
import java.util.List;

@Service
public class FavouriteService {
    @Autowired
    FavouriteRepository favouriteRepository;

    public void addComment(Favourite favourite) {
        favouriteRepository.save(favourite);
    }

    public List<Favourite> findAllByUser(User user) {
        return favouriteRepository.findAllByUser(user);
    }

    public Favourite findByPost(Post post) {
        return favouriteRepository.findByPost(post);
    }

    public void deleteFavourite(Favourite favourite) {
        favouriteRepository.delete(favourite);
    }

    public Favourite getByPostAndUser(Post post, User user) {
        return favouriteRepository.findByPostAndUser(post, user);
    }
}
