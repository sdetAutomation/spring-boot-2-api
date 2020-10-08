package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class UserIntegrationDtoTest {


    @Autowired
    private TestRestTemplate restTemplate;

    private final String path_mm = "/modelmapper/users/";

    @Test
    public void user_dto_tc0001_getByUserId_Mm() {
        String td_UserId = "101";
        String td_UserName = "darth.vader";
        String td_FirstName = "darth";
        String td_LastName = "vader";

        ResponseEntity<User> response = restTemplate.getForEntity(path_mm + "/" +td_UserId, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();

        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertNull(user.getEmail());
        assertNull(user.getRole());
        assertNull(user.getSsn());
        assertFalse(user.getOrders().isEmpty());
    }
}
