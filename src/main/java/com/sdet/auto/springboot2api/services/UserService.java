package com.sdet.auto.springboot2api.services;

import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id); // Optional<User>, return will be given id info or empty()
        return user;
    }

    public User updateUserById(Long id, User user) {
        user.setId(id); // setting the id context.
        return userRepository.save(user);
    }

    public void deleteUserById(Long id){
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        }
    }
}
