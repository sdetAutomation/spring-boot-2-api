package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.exceptions.UserExistsException;
import com.sdet.auto.springboot2api.exceptions.UserNameNotFoundException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
@Api(tags = "User RESTful Api", value = "UserController")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "retrieves list of all users")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @ApiOperation(value = "creates a user")
    @PostMapping
    public ResponseEntity<User> createUser(@ApiParam("User fields to create a new user")
                                               @Valid @RequestBody User user, UriComponentsBuilder builder) {
        try {
            userService.createUser(user);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/users/{id}").buildAndExpand(user.getUserId()).toUri());
            return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
        } catch (UserExistsException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ApiOperation(value = "get a user by id")
    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") @Min(1) Long id) {
        try {
            Optional<User> optionalUser = userService.getUserById(id);
            return optionalUser.get();
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @ApiOperation(value = "update a user by id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUserById(@PathVariable("id") Long id, @RequestBody User user) {
        try {
            return userService.updateUserById(id, user);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ApiOperation(value = "delete a user")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }

    @GetMapping("byusername/{username}")
    public User getUserByUsername(@PathVariable("username") String username) throws UserNameNotFoundException {
        User user = userService.getUserByUsername(username);
        if(user == null) {
            throw new UserNameNotFoundException("Username: " + username + " not found in User Repository");
        }
        return user;
    }
}
