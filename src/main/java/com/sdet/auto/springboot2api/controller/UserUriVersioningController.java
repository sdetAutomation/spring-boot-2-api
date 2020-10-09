package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.dto.UserDtoV1;
import com.sdet.auto.springboot2api.dto.UserDtoV2;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.Min;
import java.util.Optional;

@RestController
@RequestMapping("versioning/users")
public class UserUriVersioningController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping({"/v1/{id}", "/v1.1/{id}"})
    public UserDtoV1 getUserDtoById(@PathVariable("id") @Min(1) Long id) throws UserNotFoundException {
        Optional<User> userOptional = userService.getUserById(id); // Optional<User>, return will be given id info or empty()

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository");
        }

        User user = userOptional.get();

        // convert user to userDtoV1
        UserDtoV1 userDtoV1 = modelMapper.map(user, UserDtoV1.class);

        return userDtoV1;
    }

    @GetMapping({"/v2/{id}"})
    public UserDtoV2 getUserDtoById2(@PathVariable("id") @Min(1) Long id) throws UserNotFoundException {
        Optional<User> userOptional = userService.getUserById(id); // Optional<User>, return will be given id info or empty()

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository");
        }

        User user = userOptional.get();

        // convert user to userDtoV1
        UserDtoV2 userDtoV2 = modelMapper.map(user, UserDtoV2.class);

        return userDtoV2;
    }
}
