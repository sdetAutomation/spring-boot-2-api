package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sdet.auto.springboot2api.model.Order;
import com.sdet.auto.springboot2api.repository.OrderRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderRepository orderRepository;

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

    @Test
    public void order_tc0006_getByOrderId() {
        String td_OrderId = "2001";
        String td_OrderDescription = "order11";

        ResponseEntity<Order> response = restTemplate.getForEntity(path + "/id/" +td_OrderId, Order.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Order order = response.getBody();

        assertEquals(td_OrderId, order.getOrder_id().toString());
        assertEquals(td_OrderDescription, order.getOrder_description());
    }

    @Test
    public void order_tc0007_getByOrderId_Exception() throws IOException {
        String td_OrderId = "9999";
        String td_Error = "Not Found";
        String td_Message = "Order ID not found in Order Repository";
        String td_path = "/orders/id/" + td_OrderId;

        ResponseEntity<String> response = restTemplate.getForEntity(path + "/id/" +td_OrderId, String.class);

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
    public void order_tc0008_updateOrderById() {
        String td_OrderId = "2001";
        String td_OrderDescription = "change_description";
        String td_path = "/orders/id/" + td_OrderId;

        // making a get to get a user record
        ResponseEntity<Order> initResponse = restTemplate.getForEntity(td_path, Order.class);

        Order initOrder = initResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Order> entity = new HttpEntity<>(initOrder, headers);
        // edit user entity with updated test data values
        entity.getBody().setOrder_description(td_OrderDescription);

        // make a put call to edit the record using an api put request with updated entity
        ResponseEntity<Order> response = restTemplate.exchange(td_path, HttpMethod.PUT, entity, Order.class);

        // assert the response from the api
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Order order = response.getBody();
        // assert the response body from the put request
        assertEquals(td_OrderId, order.getOrder_id().toString());
        assertEquals(td_OrderDescription, order.getOrder_description());

        // making a getByOrderId to retrieve the user record
        ResponseEntity<Order> getResponse = restTemplate.getForEntity(td_path, Order.class);

        // assert the response body from getByUserId request
        Order updatedOrder = getResponse.getBody();
        assertEquals(td_OrderId, updatedOrder.getOrder_id().toString());
        assertEquals(td_OrderDescription, updatedOrder.getOrder_description());
    }

    @Test
    public void order_tc0009_updateOrderById_Exception() throws IOException {
        String td_OrderId = "9999";
        String td_path = "/orders/id/" + td_OrderId;
        String td_Error = "Bad Request";
        String td_Message = "Order not found in Order Repository, please provide correct order_id";

        Order entity = createOrder("");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Order> putEntity = new HttpEntity<>(entity, headers);

        ResponseEntity<String> response = restTemplate.exchange(td_path, HttpMethod.PUT, putEntity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

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
    public void order_tc0010_deleteOrderById() {
        String td_OrderId = "101";
        String td_OrderDescription = "test_description";
        // create a fresh record
        Order entity = createOrder(td_OrderDescription);
        ResponseEntity<Order> response = restTemplate.postForEntity(path + "/" + td_OrderId, entity, Order.class);
        // delete record
        ResponseEntity<String> deleteResponse = restTemplate.exchange(path + "/id/" + response.getBody().getOrder_id(),
                HttpMethod.DELETE, new HttpEntity<String>(null, new HttpHeaders()), String.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertFalse(orderRepository.existsById(response.getBody().getOrder_id()));
    }

    @Test
    public void order_tc0011_deleteOrderById_Exception() throws IOException {
        String td_UserId = "888";
        String td_Error = "Bad Request";
        String td_Message = "Order not found in Order Repository, please provide correct order_id";
        String td_path = "/orders/id/" + td_UserId;

        ResponseEntity<String> deleteResponse = restTemplate.exchange(path + "/id/" + td_UserId, HttpMethod.DELETE,
                new HttpEntity<String>(null, new HttpHeaders()), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, deleteResponse.getStatusCode());

        // getting the response body
        String body = deleteResponse.getBody();
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
