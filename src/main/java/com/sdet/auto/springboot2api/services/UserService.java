package com.sdet.auto.springboot2api.services;

import com.sdet.auto.springboot2api.exceptions.UserExistsException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    User createUser(User user) throws UserExistsException;

    Optional<User> getUserById(Long id) throws UserNotFoundException;

    User updateUserById(Long id, User user) throws UserNotFoundException;

    void deleteUserById(Long id);

    User getUserByUsername(String username);
}
