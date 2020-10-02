package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationHateoasTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    private final String path = "/hateoas/users";

    @Test
    public void user_hateoas_tc0001_getByUserId() throws IOException {
        String td_UserId = "101";
        String td_link_href = "/hateoas/users/101";

        ResponseEntity<String> response = restTemplate.getForEntity(path + "/" + td_UserId, String.class);
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);

        String link = node.get("_links").get("self").get("href").toString();
        Assert.assertThat(link, CoreMatchers.containsString(td_link_href));
    }

    @Test
    public void user_hateoas_tc0002_getAllUsers() throws IOException {
        String td_user_self_href = "/hateoas/users/101";
        String td_user_all_orders_href = "hateoas/orders/101";
        String td_selflink_all_users = "hateoas/users";
        String td_link_href = "/hateoas/users";

        ResponseEntity<String> response = restTemplate.getForEntity(path, String.class);
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);

        ObjectNode user101 = (ObjectNode) node.get("_embedded").get("userList").get(0);
        // extract hateoas links for assert
        String actual_self_link = user101.get("_links").get("self").get("href").toString();
        String actual_all_orders_link = user101.get("_links").get("all-orders").get("href").toString();

        Assert.assertThat(actual_self_link, CoreMatchers.containsString(td_user_self_href));
        Assert.assertThat(actual_all_orders_link, CoreMatchers.containsString(td_user_all_orders_href));

        String actual_selflink_all_users = node.get("_links").get("self").get("href").toString();
        Assert.assertThat(actual_selflink_all_users, CoreMatchers.containsString(td_selflink_all_users));
    }
}
