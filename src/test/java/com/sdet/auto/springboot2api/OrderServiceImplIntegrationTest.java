package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.exceptions.OrderNotFoundException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.OrderRepository;
import com.sdet.auto.springboot2api.repository.UserRepository;
import com.sdet.auto.springboot2api.services.OrderService;
import com.sdet.auto.springboot2api.services.OrderServiceImpl;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderServiceImplIntegrationTest {

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public OrderService orderService() {
            return new OrderServiceImpl();
        }
    }

    @Autowired
    private OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() throws OrderNotFoundException {
        int  first_user = 0;
        Long td_id1 = 111L;
        String td_userName1 = "td_userName1";
        String td_firstName1 = "td_firstName1";
        String td_lastName1 = "td_lastName1";
        String td_email1 = "td_email1";
        String td_role1 = "td_role1";
        String td_ssn1 = "td_ssn1";
        Long td_id2 = 222L;
        String td_userName2 = "td_userName2";
        String td_firstName2 = "td_firstName2";
        String td_lastName2 = "td_lastName2";
        String td_email2 = "td_email2";
        String td_role2 = "td_role2";
        String td_ssn2 = "td_ssn2";
        Long td_id3 = 333L;
        String td_userName3 = "td_userName3";
        String td_firstName3 = "td_firstName3";
        String td_lastName3 = "td_lastName3";
        String td_email3 = "td_email3";
        String td_role3 = "td_role3";
        String td_ssn3 = "td_ssn3";

        User user1 = new User(td_id1,td_userName1, td_firstName1, td_lastName1, td_email1, td_role1, td_ssn1);
        User user2 = new User(td_id2,td_userName2, td_firstName2, td_lastName2, td_email2, td_role2, td_ssn2);
        User user3 = new User(td_id3,td_userName3, td_firstName3, td_lastName3, td_email3, td_role3, td_ssn3);

        Order order1 = new Order(td_id1, "order1", user1);
        Order order2 = new Order(td_id2, "order2", user2);
        Order order3 = new Order(td_id3, "order3", user3);

        List<Order> ordersList = Arrays.asList(order1, order2, order3);

        // list out mocks scenarios below.
        Mockito.when(orderRepository.findAll()).thenReturn(ordersList);
        Mockito.when(userRepository.findById(td_id1)).thenReturn(Optional.of(user1));
        Mockito.when(orderRepository.findById(td_id1)).thenReturn(Optional.of(order1));
    }

    @Test
    public void Order_Service_tc0001_getAllOrders() {
        int td_firstOrder = 0;
        int td_array_size = 3;
        String td_orderId = "111";
        String td_orderDescription = "order1";

        List<Order> orders = orderService.getAllOrders();

        assertEquals(td_array_size, orders.size());
        assertEquals(td_orderId, orders.get(td_firstOrder).getOrder_id().toString());
        assertEquals(td_orderDescription, orders.get(td_firstOrder).getOrder_description());
    }

    @Test
    public void Order_Service_tc0002_getAllOrdersByUserId() throws UserNotFoundException {
        Long td_id1 = 111L;

        orderService.getAllOrdersByUserId(td_id1);

        Mockito.verify(userRepository, times(1)).findById(td_id1);
    }

    @Test
    public void Order_Service_tc0003_getAllOrdersByUserId_Exception() {
        Long td_id1 = 222L;
        String td_error_message = "User not found in User Repository, please provide correct user id";

        try {
            orderService.getAllOrdersByUserId(td_id1);
        } catch (UserNotFoundException ex) {
            assertEquals(td_error_message, ex.getMessage());
        }

        Mockito.verify(userRepository, times(1)).findById(td_id1);
    }

    @Test
    public void Order_Service_tc0004_createOrder() throws UserNotFoundException {
        Long td_id3 = 111L;

        User user = new User(null, "", "", "", "", "", "");

        Order order1 = new Order(td_id3, "order1", user);

        orderService.createOrder(td_id3, order1);

        Mockito.verify(userRepository, times(1)).findById(td_id3);
        Mockito.verify(orderRepository, times(1)).save(order1);
    }

    @Test
    public void Order_Service_tc0005_createOrder_Exception() {
        Long td_id2 = 222L;
        String td_error_message = "User not found in User Repository";

        User user = new User(null, "", "", "", "", "", "");

        Order order1 = new Order(td_id2, "order1", user);

        try {
            orderService.createOrder(td_id2, order1);
        } catch (UserNotFoundException ex) {
            assertEquals(td_error_message, ex.getMessage());
        }

        Mockito.verify(userRepository, times(1)).findById(td_id2);
        Mockito.verify(orderRepository, times(0)).save(order1);
    }

    @Test
    public void Order_Service_tc0006_getOrderById() throws OrderNotFoundException {
        Long td_orderId = 111L;
        String td_orderDescription = "order1";

        Optional<Order> orders = orderService.getOrderById(td_orderId);

        assertEquals(td_orderId, orders.get().getOrder_id());
        assertEquals(td_orderDescription, orders.get().getOrder_description());
    }

    @Test
    public void Order_Service_tc0007_getOrderById_Exception()  {
        Long td_orderId = 222L;
        String td_error_message = "Order ID not found in Order Repository";

        try {
            orderService.getOrderById(td_orderId);
        } catch (OrderNotFoundException ex){
            assertEquals(td_error_message, ex.getMessage());
        }
    }

    @Test
    public void Order_Service_tc0008_updateOrderById() throws OrderNotFoundException {
        Long td_id1 = 111L;

        User user = new User(null, "", "", "", "", "", "");

        Order order1 = new Order(td_id1, "order1", user);

        orderService.updateOrderById(td_id1, order1);

        Mockito.verify(orderRepository, times(1)).findById(td_id1);
        Mockito.verify(orderRepository, times(1)).save(order1);
    }

    @Test
    public void Order_Service_tc0009_updateOrderById_Exception() {
        Long td_id1 = 222L;
        String td_error_message = "Order not found in Order Repository, please provide correct order_id";

        User user = new User(null, "", "", "", "", "", "");
        Order order1 = new Order(td_id1, "order1", user);

        try {
            orderService.updateOrderById(td_id1, order1);
        } catch (OrderNotFoundException ex){
            assertEquals(td_error_message, ex.getMessage());
        }
    }

    @Test
    public void Order_Service_tc0010_deleteOrder() {
        Long td_id = 111L;
        orderService.deleteOrderById(td_id);

        Mockito.verify(orderRepository, times(1)).deleteById(td_id);
    }

    @Test
    public void Order_Service_tc0011_deleteOrder_Exception() {
        Long td_id = 999L;
        String td_error_message = "400 BAD_REQUEST \"Order not found in Order Repository, please provide correct order_id\"";

        try {
            orderService.deleteOrderById(td_id);
        } catch (ResponseStatusException ex) {
            assertEquals(td_error_message, ex.getMessage());
        }
    }
}
