package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.model.User;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String path = "/orders";

    @Test
    public void order_tc0001_getAllOrdersByUserId() {
        String td_UserId = "101";
        Long td_OrderId = Long.valueOf(2001);
        String td_OrderDescription = "order11";
        int td_ExpectedNumOfRecords = 3;
        int firstRecordFromResponse = 0;

        ResponseEntity<Order[]> response = restTemplate.getForEntity(path + "/" + td_UserId, Order[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // getting first order from response body
        Order order = response.getBody()[firstRecordFromResponse];

        assertEquals(td_ExpectedNumOfRecords, response.getBody().length);
        assertEquals(td_OrderId, order.getOrder_id());
        assertEquals(td_OrderDescription, order.getOrder_description());
    }

    @Test
    public void order_tc0002_getAllOrdersByUserId_Exception() throws IOException {
        String td_UserId = "1099";
        String td_Error = "Not Found";
        String td_Message = "User not found in User Repository, please provide correct user id";
        String td_path = path + "/" + td_UserId;

        ResponseEntity<String> response = restTemplate.getForEntity(td_path, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // assert expected vs actual
        assertEquals(td_Error, node.get("error").asText());
        assertEquals(td_Message, node.get("message").asText());
        assertEquals(td_path, node.get("path").asText());
    }

    @Test
    public void order_tc0003_getAllOrders() {
        Long td_OrderId = Long.valueOf(2001);
        String td_OrderDescription = "order11";
        int td_ExpectedNumOfRecords = 6;
        int firstRecordFromResponse = 0;

        ResponseEntity<Order[]> response = restTemplate.getForEntity(path, Order[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        // getting first order from response body
        Order order = response.getBody()[firstRecordFromResponse];

        assertEquals(td_ExpectedNumOfRecords, response.getBody().length);
        assertEquals(td_OrderId, order.getOrder_id());
        assertEquals(td_OrderDescription, order.getOrder_description());
    }

    @Test
    public void order_tc0004_createOrder() {
        String td_UserId = "101";
        String td_OrderDescription = "test_description";
        String td_header = "/orders/";

        Order entity = createOrder(td_OrderDescription);
        ResponseEntity<Order> response = restTemplate.postForEntity(path + "/" + td_UserId, entity, Order.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Order order = response.getBody();

        assertTrue(order.getOrder_id() > 0);
        assertEquals(td_OrderDescription, order.getOrder_description());

        // get header from response
        HttpHeaders header = response.getHeaders();
        // assert expected header matches actual
        assertEquals(td_header + td_UserId, header.getLocation().getPath());
    }

    @Test
    public void order_tc0005_createOrder_Exception() throws IOException {
        String td_UserId = "888";
        String td_OrderDescription = "test_description";
        String td_Error = "Not Found";
        String td_Message = "User not found in User Repository";
        String td_path = "/orders/" + td_UserId;

        Order entity = createOrder(td_OrderDescription);
        ResponseEntity<String> response = restTemplate.postForEntity(td_path, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // assert expected vs actual
        assertEquals(td_Error, node.get("error").asText());
        assertEquals(td_Message, node.get("message").asText());
        assertEquals(td_path, node.get("path").asText());
    }

    private Order createOrder(String orderDescription) {
        Order order = new Order();
        order.setOrder_description(orderDescription);
        return order;
    }
}