package com.sdet.auto.springboot2api.services;

import com.sdet.auto.springboot2api.exceptions.OrderNotFoundException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> getAllOrders();

    List<Order> getAllOrdersByUserId(Long id) throws UserNotFoundException;

    Order createOrder(Long id, Order order) throws UserNotFoundException;

    Optional<Order> getOrderById(Long id) throws OrderNotFoundException;

    Order updateOrderById(Long id, Order order) throws OrderNotFoundException;

    void deleteOrderById(Long id);
}
