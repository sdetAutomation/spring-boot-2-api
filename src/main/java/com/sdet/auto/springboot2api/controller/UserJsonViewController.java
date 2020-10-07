package com.sdet.auto.springboot2api.controller;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonView;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.model.Views;
import com.sdet.auto.springboot2api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/jsonview/users")
public class UserJsonViewController {

    @Autowired
    private UserService userService;

    @JsonView(Views.External.class)
    @GetMapping("/external/{id}")
    public Optional<User> getUserByIdExternal(@PathVariable("id") @Min(1) Long id) {

        try {
            return userService.getUserById(id);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @JsonView(Views.Internal.class)
    @GetMapping("/internal/{id}")
    public Optional<User> getUserByIdInternal(@PathVariable("id") @Min(1) Long id) {
        try {
            return userService.getUserById(id);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
