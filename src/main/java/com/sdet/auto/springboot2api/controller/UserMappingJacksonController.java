package com.sdet.auto.springboot2api.controller;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/jacksonfilter/users")
public class UserMappingJacksonController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public MappingJacksonValue getUserById(@PathVariable("id") @Min(1) Long id) {
        try {
            Optional<User> userOptional = userService.getUserById(id);
            User user = userOptional.get();

            // define which fields will be seen
            Set<String> fields = new HashSet<>();
            fields.add("userId");
            fields.add("username");
            fields.add("ssn");
            fields.add("orders");

            FilterProvider filterProvider = new SimpleFilterProvider()
                    .addFilter("userFilter", SimpleBeanPropertyFilter.filterOutAllExcept(fields));

            // now create jackson mapping value
            MappingJacksonValue mapper = new MappingJacksonValue(user);

            mapper.setFilters(filterProvider);
            return mapper;

        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
