package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.dto.UserMmDto;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("modelmapper/users")
public class UserModelMapperController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserMmDto getUserDtoById(@PathVariable("id") @Min(1) Long id) {
        try {
            return userService.getUserByIdMm(id);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
