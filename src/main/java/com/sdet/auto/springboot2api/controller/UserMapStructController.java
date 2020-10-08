package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.dto.UserMsDto;
import com.sdet.auto.springboot2api.mappers.UserMapper;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/mapstruct/users")
public class UserMapStructController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public List<UserMsDto> getAllUsersDto() {
        return userMapper.userToUserDto(userRepository.findAll());

    }
}
