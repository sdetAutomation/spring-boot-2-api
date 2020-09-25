package com.sdet.auto.springboot2api.controller;

import com.sdet.auto.springboot2api.exceptions.OrderNotFoundException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping
    public List<Order> getAllUsers() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{userId}")
    public List<Order> getAllOrdersByUserId(@PathVariable Long userId) {
        try {
            return orderService.getAllOrdersByUserId(userId);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Order> createOrder(@PathVariable Long userId, @RequestBody Order order,
                                             UriComponentsBuilder builder) {
        try {
            Order orderObj = orderService.createOrder(userId, order);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/orders/{id}").buildAndExpand(userId).toUri());
            return new ResponseEntity<>(orderObj, headers, HttpStatus.CREATED);
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("id/{orderId}")
    public Optional<Order> getOrderById(@PathVariable("orderId") @Min(1) Long id) {
        try {
            return orderService.getOrderById(id);
        } catch (OrderNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}