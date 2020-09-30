package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.OrderRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DataJpaTest
public class OrderRepositoryIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Before
    public void setUp() {

        User user = new User(null, "username1", "firstname1", "lastname1",
                "email1", "role1", "ssn1", null);
        User user1 = user;
        User user2 = new User(null,"username2", "firstname2", "lastname2",
                "email2", "role2", "ssn2", null);


        Order order1 = new Order(null, "order1", user1);
        Order order2 = new Order(null, "order2", user2);
        // perform save
        this.orderRepository.save(order1);
        this.orderRepository.save(order2);
    }

    @Test
    public void Order_Repository_TC0001_testFetchData() {
        Optional<Order> saved_order1 = orderRepository.findById(1L);
        Optional<Order> saved_order2 = orderRepository.findById(2L);
        // assert order1
        assertEquals("1", saved_order1.get().getOrder_id().toString());
        assertEquals("order1", saved_order1.get().getOrder_description());
        // assert order2
        assertEquals("2", saved_order2.get().getOrder_id().toString());
        assertEquals("order2", saved_order2.get().getOrder_description());
    }

    @Test
    public void Order_Repository_TC0002_testUpdateData() {
        Long td_original_record = 3L;
        String td_orderDescription = "td_orderDescription";
        User user1 = new User(null,"username1", "firstname1", "lastname1",
                "email1", "role1", "ssn1", null);
        // get record
        Optional<Order> saved_order_1 = orderRepository.findById(td_original_record);
        // creating order obj to send with update
        Order order = new Order(saved_order_1.get().getOrder_id(), td_orderDescription, user1);
        // save order
        orderRepository.save(order);
        // get updated order
        Optional<Order> updated_order = orderRepository.findById(order.getOrder_id());

        assertEquals(order.getOrder_id(), updated_order.get().getOrder_id());
        assertEquals(td_orderDescription, updated_order.get().getOrder_description());
    }

    @Test
    public void Order_Repository_TC0003_deleteTestData() {
        Long td_orderId = 5L;
        orderRepository.deleteById(td_orderId);
        Optional<Order> order = orderRepository.findById(td_orderId);

        assertEquals(order.orElse(null), null);
    }
}
