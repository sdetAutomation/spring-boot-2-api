package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.UserRepository;
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
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    private final String path = "/users";

    private final String path_mm = "/modelmapper/users/";

    private final String path_ms = "/mapstruct/users/";

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
        String td_header = "/users/";

        User entity = createUser(td_UserName, td_FirstName, td_LastName, td_Email, td_Role, td_ssn);
        ResponseEntity<User> response = restTemplate.postForEntity(path, entity, User.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        User user = response.getBody();

        assertTrue(user.getUserId() > 0);
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());

        // get header from response
        HttpHeaders header = response.getHeaders();
        // assert expected header matches actual
        assertEquals(td_header + user.getUserId(), header.getLocation().getPath());
    }

    @Test
    public void user_tc0003_getByUserId() {
        String td_UserId = "101";
        String td_UserName = "darth.vader";
        String td_FirstName = "darth";
        String td_LastName = "vader";
        String td_Email = "darth.vader@gmail.com";
        String td_Role = "admin";
        String td_ssn = "ssn-01-0000";

        ResponseEntity<User> response = restTemplate.getForEntity(path + "/" +td_UserId, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();

        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());
    }

    @Test
    public void user_tc0004_updateUserById() {
        String td_UserId = "101";
        String td_UserName = "darth.vader_updated";
        String td_FirstName = "darth_updated";
        String td_LastName = "vader_updated";
        String td_Email = "darth.vader_updated@gmail.com";
        String td_Role = "admin_updated";
        String td_ssn = "ssn-01-0001";
        // making a get to get a user record
        ResponseEntity<User> initResponse = restTemplate.getForEntity(path + "/" + td_UserId, User.class);

        User initUser = initResponse.getBody();

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> entity = new HttpEntity<>(initUser, headers);
        // edit user entity with updated test data values
        entity.getBody().setUsername(td_UserName);
        entity.getBody().setFirstname(td_FirstName);
        entity.getBody().setLastname(td_LastName);
        entity.getBody().setEmail(td_Email);
        entity.getBody().setRole(td_Role);
        entity.getBody().setSsn(td_ssn);

        // make a put call to edit the record using an api put request with updated entity

        ResponseEntity<User> response = restTemplate.exchange(path + "/" + entity.getBody().getUserId(), HttpMethod.PUT,
                entity, User.class);

        // assert the response from the api
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        User user = response.getBody();
        // assert the response body from the put request
        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());

        // making a getByUserId to retrieve the user record
        ResponseEntity<User> getResponse = restTemplate.getForEntity(path + "/" + td_UserId, User.class);

        // assert the response body from getByUserId request
        User updatedUser = getResponse.getBody();
        assertEquals(td_UserId, updatedUser.getUserId().toString());
        assertEquals(td_UserName, updatedUser.getUsername());
        assertEquals(td_FirstName, updatedUser.getFirstname());
        assertEquals(td_LastName, updatedUser.getLastname());
        assertEquals(td_Email, updatedUser.getEmail());
        assertEquals(td_Role, updatedUser.getRole());
        assertEquals(td_ssn, updatedUser.getSsn());
    }

    @Test
    public void user_tc0005_deleteUserById() {
        String td_UserName = "captain.marvel";
        String td_FirstName = "captain";
        String td_LastName = "marvel";
        String td_Email = "captain.marvel@gmail.com";
        String td_Role = "admin";
        String td_ssn = "ssn-05-0000";

        User entity = createUser(td_UserName, td_FirstName, td_LastName, td_Email, td_Role, td_ssn);
        ResponseEntity<User> response = restTemplate.postForEntity(path, entity, User.class);

        ResponseEntity<String> deleteResponse = restTemplate.exchange(path + "/" + response.getBody().getUserId(),
                HttpMethod.DELETE, new HttpEntity<String>(null, new HttpHeaders()), String.class);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertFalse(userRepository.existsById(response.getBody().getUserId()));
    }

    @Test
    public void user_tc0006_getByUsername() {
        String td_UserId = "103";
        String td_UserName = "thor.odinson";
        String td_FirstName = "thor";
        String td_LastName = "odinson";
        String td_Email = "thor@gmail.com";
        String td_Role = "admin";
        String td_ssn = "ssn-03-0000";

        ResponseEntity<User> response = restTemplate.getForEntity(path + "/byusername/" + td_UserName, User.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        User user = response.getBody();

        assertEquals(td_UserId, user.getUserId().toString());
        assertEquals(td_UserName, user.getUsername());
        assertEquals(td_FirstName, user.getFirstname());
        assertEquals(td_LastName, user.getLastname());
        assertEquals(td_Email, user.getEmail());
        assertEquals(td_Role, user.getRole());
        assertEquals(td_ssn, user.getSsn());
    }

    @Test
    public void user_tc0007_getByUserId_Exception() throws IOException {
        String td_UserId = "1001";
        String td_Error = "Not Found";
        String td_Message = "User not found in User Repository";
        String td_path = "/users/" + td_UserId;
        // since response will not be a user object, casting the response as a String so we can map to an object
        ResponseEntity<String> response = restTemplate.getForEntity(path + "/" + td_UserId, String.class);

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
    public void user_tc0008_updateUserById_Exception() throws IOException {
        String td_UserId = "1001";
        String td_Error = "Bad Request";
        String td_Message = "User not found in User Repository, please provide correct user id";
        String td_path = "/users/" + td_UserId;
        // creating user entity for put
        User entity = createUser("", "", "", "", "", "");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> putEntity = new HttpEntity<>(entity, headers);

        // make a put call to edit the record using an api put request
        ResponseEntity<String> response = restTemplate.exchange(path + "/" + td_UserId, HttpMethod.PUT,
                putEntity, String.class);

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
    public void user_tc0009_deleteUserById_Exception() throws IOException {
        String td_UserId = "1001";
        String td_Error = "Bad Request";
        String td_Message = "User not found in User Repository, please provide correct user id";
        String td_path = "/users/" + td_UserId;

        ResponseEntity<String> deleteResponse = restTemplate.exchange(path + "/" + td_UserId, HttpMethod.DELETE,
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

    @Test
    public void user_tc0010_createUser_Exception() throws IOException {
        String td_UserName = "thor.odinson";
        String td_FirstName = "thor";
        String td_LastName = "odinson";
        String td_Error = "Bad Request";
        String td_Message = "User already exists in User Repository";
        String td_path = "/users";

        User entity = createUser(td_UserName, td_FirstName, td_LastName, "", "", "");
        ResponseEntity<String> response = restTemplate.postForEntity(path, entity, String.class);

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
    public void user_tc0011_updateUserById_patch_CustomException() throws IOException {
        String td_UserId = "102";
        String td_UserName = "thor.odinson";
        String td_FirstName = "thor";
        String td_ErrorDetails = "Request method 'PATCH' not supported";
        String td_Message = "From HttpRequestMethodNotSupported in GEH - Method Not Allowed";
        // creating user entity for put
        User entity = createUser(td_UserName, td_FirstName, "", "", "", "");

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<User> putEntity = new HttpEntity<>(entity, headers);

        // make a post call and set patch in the url path due to bug with restTemplate and patch function
        ResponseEntity<String> response = restTemplate.postForEntity(path + "/" + td_UserId + "?_method=patch",
                entity, String.class);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // assert expected vs actual
        assertEquals(td_ErrorDetails, node.get("errordetails").asText());
        assertEquals(td_Message, node.get("message").asText());
    }

    @Test
    public void user_tc0012_getByUsername_CustomException() throws IOException {
        String td_UserName = "bad.username";
        String td_ErrorDetails = "uri=/users/byusername/" + td_UserName;
        String td_Message = "Username: " + td_UserName + " not found in User Repository";

        // since response will not be a user object, casting the response as a String so we can map to an object
        ResponseEntity<String> response = restTemplate.getForEntity(path + "/byusername/" + td_UserName, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // assert expected vs actual
        assertEquals(td_ErrorDetails, node.get("errordetails").asText());
        assertEquals(td_Message, node.get("message").asText());
    }

    @Test
    public void user_tc0013_getByUserId_constraint_CustomException() throws IOException {
        // pass in id < 1, which will throw an exception
        String td_UserId = "0";
        String td_ErrorDetails = "uri=/users/0";
        String td_Message = "getUserById.id: must be greater than or equal to 1";
        // since response will not be a user object, casting the response as a String so we can map to an object
        ResponseEntity<String> response = restTemplate.getForEntity(path + "/" + td_UserId, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        // getting the response body
        String body = response.getBody();
        // get fields from JSON using Jackson Object Mapper
        final ObjectNode node = new ObjectMapper().readValue(body, ObjectNode.class);
        // assert expected vs actual
        assertEquals(td_ErrorDetails, node.get("errordetails").asText());
        assertEquals(td_Message, node.get("message").asText());
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

    @Test
    public void user_dto_tc0002_getAllUsers_Ms() throws IOException {
        ResponseEntity<List> response = restTemplate.getForEntity(path_ms, List.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());

        String body = response.getBody().get(0).toString();
        // assert expected DTO field names
        assertThat(body, containsString("userId"));
        assertThat(body, containsString("userName"));
        assertThat(body, containsString("emailAddress"));
    }
}

