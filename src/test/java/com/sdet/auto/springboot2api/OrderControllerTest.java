package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdet.auto.springboot2api.controller.OrderController;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.OrderService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Test
    public void Order_Controller_tc0001_getAllOrders() throws Exception {
        Long td_id1 = 111L;
        Long td_id2 = 222L;
        Long td_id3 = 333L;

        User user = new User(null, "", "", "", "", "", "");

        Order order1 = new Order(td_id1, "order1", user);
        Order order2 = new Order(td_id2, "order2", user);
        Order order3 = new Order(td_id3, "order3", user);

        List<Order> ordersList = Arrays.asList(order1, order2, order3);

        given(orderService.getAllOrders()).willReturn(ordersList);

        mockMvc.perform(get("/orders")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].order_id").value(td_id1))
                .andExpect(jsonPath("$[0].order_description").value("order1"))
                .andExpect(jsonPath("$[1].order_id").value(td_id2))
                .andExpect(jsonPath("$[1].order_description").value("order2"))
                .andExpect(jsonPath("$[2].order_id").value(td_id3))
                .andExpect(jsonPath("$[2].order_description").value("order3"));
    }

    @Test
    public void Order_Controller_tc0002_getAllOrdersByUserId() throws Exception {
        Long td_id1 = 111L;
        Long td_id2 = 222L;
        Long td_id3 = 333L;

        User user = new User(null, "", "", "", "", "", "");

        Order order1 = new Order(td_id1, "order1", user);
        Order order2 = new Order(td_id2, "order2", user);
        Order order3 = new Order(td_id3, "order3", user);

        List<Order> ordersList = Arrays.asList(order1, order2, order3);

        given(orderService.getAllOrdersByUserId(td_id1)).willReturn(ordersList);

        mockMvc.perform(get("/orders/111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].order_id").value(td_id1))
                .andExpect(jsonPath("$[0].order_description").value("order1"))
                .andExpect(jsonPath("$[1].order_id").value(td_id2))
                .andExpect(jsonPath("$[1].order_description").value("order2"))
                .andExpect(jsonPath("$[2].order_id").value(td_id3))
                .andExpect(jsonPath("$[2].order_description").value("order3"));
    }

//    @Test
//    public void Order_Controller_tc0003_getAllOrdersByUserId_Exception() throws Exception {
//        Long td_id1 = 111L;
//
//        given(orderService.getAllOrdersByUserId(td_id1)).willThrow(new UserNotFoundException(""));
//
//        mockMvc.perform(get("/orders/111")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }

    @Test
    public void Order_Controller_tc0003_createOrder() throws Exception {
        Long td_id = 222L;
        User user = new User(td_id, "", "", "", "", "", "");
        Order order = new Order(td_id, "order1", user);

        ObjectMapper objectMapper = new ObjectMapper();
        String userAsString = objectMapper.writeValueAsString(order);

        mockMvc.perform(post("/orders/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(userAsString))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void Order_Controller_tc0004_getOrderById() throws Exception {
        Long td_id = 111L;
        User user = new User(null, "", "", "", "", "", "");
        Order order = new Order(td_id, "order1", user);

        given(orderService.getOrderById(td_id)).willReturn(java.util.Optional.of(order));

        mockMvc.perform(get("/orders/id/111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.order_id").value(td_id))
                .andExpect(jsonPath("$.order_description").value("order1"));
    }

    @Test
    public void Order_Controller_tc0005_updateOrderById() throws Exception {
        Long td_id = 222L;
        User user = new User(td_id, "", "", "", "", "", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String userAsString = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/orders/id/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(userAsString))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void Order_Controller_tc0006_deleteOrderById() throws Exception {
        mockMvc.perform(delete("/orders/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}