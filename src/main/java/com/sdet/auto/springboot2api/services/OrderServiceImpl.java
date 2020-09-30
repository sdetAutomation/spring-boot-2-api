package com.sdet.auto.springboot2api.services;

import com.sdet.auto.springboot2api.exceptions.OrderNotFoundException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.OrderRepository;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getAllOrdersByUserId(Long id) throws UserNotFoundException {
        // logic to check repository if user is present
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository, please provide correct user id");
        }
        return userOptional.get().getOrders() ;
    }

    @Override
    public Order createOrder(Long id, Order order) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id); // Optional<User>, return will be given id info or empty()

        if (!user.isPresent()) {
            throw new UserNotFoundException("User not found in User Repository");
        }
        // if it gets here, it means a user is found, we will set user for the request.
        User userObj = user.get();
        // set user found to user in post post object.
        order.setUser(userObj);

        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(Long id) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(id); // Optional<Order>, return will be given id info or empty()

        if (!order.isPresent()) {
            throw new OrderNotFoundException("Order ID not found in Order Repository");
        }
        return order;
    }

    @Override
    public Order updateOrderById(Long id, Order order) throws OrderNotFoundException {
        // logic to check repository if order is present
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            throw new OrderNotFoundException("Order not found in Order Repository, please provide correct order_id");
        }

        order.setOrder_id(id); // setting the id context.
        order.setOrder_description(order.getOrder_description());  // setting the order description
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrderById(Long id) {
        // logic to check repository if user is present
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (!optionalOrder.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not found in Order Repository, please " +
                    "provide correct order_id");
        }
        orderRepository.deleteById(id);
    }
}
