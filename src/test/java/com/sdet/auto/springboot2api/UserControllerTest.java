package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.model.User;
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
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private final String path = "/users";

    @Test
    public void user_tc0001_getAllUsers() {
        ResponseEntity<List> response = restTemplate.getForEntity(path, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    public void user_tc0002_createUser() {
        String td_UserName = "wonder.woman";
        String td_FirstName = "wonder";
        String td_LastName = "woman";
        String td_Email = "wonder.woman@gmail.com";
        String td_Role = "admin";
        String td_ssn = "ssn-04-0000";

        User entity = createUser(td_UserName, td_FirstName, td_LastName, td_Email, td_Role, td_ssn);
        ResponseEntity<User> response = restTemplate.postForEntity(path, entity, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User user = response.getBody();

        assertTrue(user.getId() > 0);
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());
    }

    private User createUser(String userName, String firstName, String lastName, String email, String role, String ssn) {
        User user = new User();
        user.setUsername(userName);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setEmail(email);
        user.setRole(role);
        user.setSsn(ssn);
        return user;
    }
}

