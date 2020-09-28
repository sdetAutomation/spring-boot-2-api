package com.sdet.auto.springboot2api.services;

import com.sdet.auto.springboot2api.exceptions.UserExistsException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) throws UserExistsException {
        // logic to check repository if user is present
        User existingUser = userRepository.findByUsername(user.getUsername());
        // if user exists, throws an exception
        if(existingUser != null){
            throw new UserExistsException("User already exists in User Repository");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id); // Optional<User>, return will be given id info or empty()

        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository");
        }
        return user;
    }

    @Override
    public User updateUserById(Long id, User user) throws UserNotFoundException {
        // logic to check repository if user is present
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository, please provide correct user id");
        }
        user.setId(id); // setting the id context.
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(Long id) {
        // logic to check repository if user is present
        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found in User Repository, please " +
                    "provide correct user id");
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}