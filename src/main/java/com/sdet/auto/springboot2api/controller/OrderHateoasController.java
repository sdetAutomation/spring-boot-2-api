package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/hateoas/orders")
public class OrderHateoasController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Resources<Order> getAllOrders() {

        List<Order> allOrders = orderService.getAllOrders();
        Resources<Order> finalResources = new Resources<>(allOrders);

        return finalResources;
    }

    @GetMapping("/{userId}")
    public Resources<Order> getAllOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> allOrders = orderService.getAllOrdersByUserId(userId);
            Resources<Order> finalResources = new Resources<>(allOrders);
            return finalResources;
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
