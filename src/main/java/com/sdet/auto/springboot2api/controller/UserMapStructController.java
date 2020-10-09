package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.dto.UserMsDto;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/mapstruct/users")
public class UserMapStructController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserMsDto> getAllUsersDto() {
        return userService.getAllUsersMs();
    }

    @GetMapping("/{userId}")
    public UserMsDto getUserByIdDto(@PathVariable Long userId) throws UserNotFoundException {
        return userService.getUserByIdMs(userId);
    }
}
