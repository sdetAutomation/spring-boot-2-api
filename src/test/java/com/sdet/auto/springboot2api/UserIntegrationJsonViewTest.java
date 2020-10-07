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
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserIntegrationJsonViewTest {

    @Autowired
    private TestRestTemplate restTemplate;


    private final String internal_path = "/jsonview/users/internal/";

    private final String external_path = "/jsonview/users/external/";

    @Test
    public void user_jsonView_tc0001_getByUserId_internal() {
        String td_UserId = "101";
        String td_UserName = "darth.vader";
        String td_FirstName = "darth";
        String td_LastName = "vader";
        String td_Email = "darth.vader@gmail.com";
        String td_Role = "admin";
        String td_ssn = "ssn-01-0000";

        ResponseEntity<User> response = restTemplate.getForEntity(internal_path + "/" +td_UserId, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();

        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());
        assertFalse(user.getOrders().isEmpty());
    }

    @Test
    public void user_jsonView_tc0001_getByUserId_external() {
        String td_UserId = "101";
        String td_UserName = "darth.vader";
        String td_FirstName = "darth";
        String td_LastName = "vader";
        String td_Email = "darth.vader@gmail.com";

        ResponseEntity<User> response = restTemplate.getForEntity(external_path + "/" +td_UserId, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();

        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertNull(user.getRole());
        assertNull(user.getSsn());
        assertNull(user.getOrders());
    }
}
