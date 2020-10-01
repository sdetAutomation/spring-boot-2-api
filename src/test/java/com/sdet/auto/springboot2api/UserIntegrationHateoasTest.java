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
}
