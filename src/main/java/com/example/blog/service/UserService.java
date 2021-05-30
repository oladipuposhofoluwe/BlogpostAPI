package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public void addUser(User newUser) {
        userRepository.save(newUser);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll(int number) {
        return userRepository.findAllByPersonDeactivatedEquals(number);
    }

    public User getUserByUsernameAndPassword(String username, String password) {
        return userRepository.getByUsernameAndPassword(username, password);
    }

    public boolean deleteUser(Optional<User> person){
        boolean flag = false;
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        try {
            Optional<User> user = userRepository.findById(person.get().getId());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 1);
            String presentDate = DateFor.format(calendar.getTime());
            user.get().setPersonDeactivated(1);
            user.get().setRemoveDate(presentDate);
            userRepository.save(user.get());
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    public void deactivatedPersonScheduler() {
        List<User> persons = userRepository.findAllByPersonDeactivated(1);
        Date date = new Date();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        System.out.println("scheduler working");
        persons.forEach(person -> {
            String presentDate = DateFor.format(date);
            String deleteDate = person.getRemoveDate();
            int actionDelete = presentDate.compareTo(deleteDate);
            if (actionDelete > 0 || actionDelete == 0) {
                System.out.println("user finally deleted");
                person.setIsDeleted(1);
                userRepository.save(person);
            }
        });
        System.out.println("The list of scheduled for deletion"+persons);
    }

    public String reverseDelete(User user){
        String flag = "";
        if(user.getPersonDeactivated() == 1){
            user.setPersonDeactivated(0);
            userRepository.save(user);
            flag = "Delete action successfully reversed";
        }
        return flag;
    }
}
