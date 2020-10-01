package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/hateoas/users")
@Validated
public class UserHateoasController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Resource<User> getUserById(@PathVariable("id") @Min(1) Long id) {

        try {
            Optional<User> userOptional = userService.getUserById(id);

            User user = userOptional.get();
            Long userid = user.getUserId();

            Link selflink = ControllerLinkBuilder.linkTo(this.getClass()).slash(userid).withSelfRel();
            user.add(selflink);

            Resource<User> finalResource = new Resource<User>(user);
            return finalResource;
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
