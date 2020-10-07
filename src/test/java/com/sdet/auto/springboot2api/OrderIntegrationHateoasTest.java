package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sdet.auto.springboot2api.repository.OrderRepository;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderIntegrationHateoasTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private final String path = "/hateoas/orders/";

    @Test
    public void order_hateoas_tc0001_getAllOrders() throws IOException {
        int td_ExpectedNumOfRecords = 6;

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // getting number of records within orders array
        int actualSize = node.get("_embedded").get("orderList").size();
        // assert response code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert record number correct
        assertEquals(td_ExpectedNumOfRecords, actualSize);
    }

    @Test
    public void order_hateoas_tc0002_getAllOrdersByUserId() throws IOException {
        String td_UserId = "101";
        int td_ExpectedNumOfRecords = 3;

        ResponseEntity<String> response = restTemplate.getForEntity(path + td_UserId, String.class);
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // getting number of records within orders array
        int actualSize = node.get("_embedded").get("orderList").size();
        // assert response code
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // assert record number correct
        assertEquals(td_ExpectedNumOfRecords, actualSize);
    }
}
