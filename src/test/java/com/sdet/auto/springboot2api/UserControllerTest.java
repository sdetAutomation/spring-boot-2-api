package com.sdet.auto.springboot2api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdet.auto.springboot2api.controller.UserController;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.services.UserService;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void User_Controller_tc0001_getAllUsers() throws Exception {
        Long td_id = 111L;
        String td_userName = "td_userName1";
        String td_firstName = "td_firstName1";
        String td_lastName = "td_lastName1";
        String td_email = "td_email1";
        String td_role = "td_role1";
        String td_ssn = "td_ssn1";

        User user = new User(td_id,td_userName, td_firstName, td_lastName, td_email, td_role, td_ssn);

        List<User> allUsers = Arrays.asList(user);

        given(userService.getAllUsers()).willReturn(allUsers);

        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(td_id))
                .andExpect(jsonPath("$[0].username").value(td_userName))
                .andExpect(jsonPath("$[0].firstname").value(td_firstName))
                .andExpect(jsonPath("$[0].lastname").value(td_lastName))
                .andExpect(jsonPath("$[0].email").value(td_email))
                .andExpect(jsonPath("$[0].role").value(td_role))
                .andExpect(jsonPath("$[0].ssn").value(td_ssn))
                .andExpect(jsonPath("$[0].orders").isEmpty());
    }

    @Test
    public void User_Controller_tc0002_createUser() throws Exception {
        Long td_id = 222L;
        String td_userName = "td_userName2";
        String td_firstName = "td_firstName2";
        String td_lastName = "td_lastName2";
        String td_email = "td_email2";
        String td_role = "td_role2";
        String td_ssn = "td_ssn2";

        User user = new User(td_id, td_userName, td_firstName, td_lastName, td_email, td_role, td_ssn);

        ObjectMapper objectMapper = new ObjectMapper();

        String userAsString = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(userAsString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(td_id))
                .andExpect(jsonPath("$.username").value(td_userName))
                .andExpect(jsonPath("$.firstname").value(td_firstName))
                .andExpect(jsonPath("$.lastname").value(td_lastName))
                .andExpect(jsonPath("$.email").value(td_email))
                .andExpect(jsonPath("$.role").value(td_role))
                .andExpect(jsonPath("$.ssn").value(td_ssn));
    }

    @Test
    public void User_Controller_tc0003_getUserById() throws Exception {
        Long td_id = 111L;
        String td_userName = "td_userName1";
        String td_firstName = "td_firstName1";
        String td_lastName = "td_lastName1";
        String td_email = "td_email1";
        String td_role = "td_role1";
        String td_ssn = "td_ssn1";

        User user = new User(td_id,td_userName, td_firstName, td_lastName, td_email, td_role, td_ssn);

        given(userService.getUserById(td_id)).willReturn(java.util.Optional.of(user));

        mockMvc.perform(get("/users/111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(td_id))
                .andExpect(jsonPath("$.username").value(td_userName))
                .andExpect(jsonPath("$.firstname").value(td_firstName))
                .andExpect(jsonPath("$.lastname").value(td_lastName))
                .andExpect(jsonPath("$.email").value(td_email))
                .andExpect(jsonPath("$.role").value(td_role))
                .andExpect(jsonPath("$.ssn").value(td_ssn))
                .andExpect(jsonPath("$.orders").isEmpty());
    }

    @Test
    public void User_Controller_tc0004_updateUserById() throws Exception {
        Long td_id = 222L;
        User user = new User(td_id, "", "", "", "", "", "");

        ObjectMapper objectMapper = new ObjectMapper();
        String userAsString = objectMapper.writeValueAsString(user);

        mockMvc.perform(put("/users/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(userAsString))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void User_Controller_tc0005_deleteUserById() throws Exception {
        mockMvc.perform(delete("/users/222")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    public void User_Controller_tc0006_getUserByUsername() throws Exception {
        Long td_id = 111L;
        String td_userName = "td_userName2";
        String td_firstName = "td_firstName1";
        String td_lastName = "td_lastName1";
        String td_email = "td_email1";
        String td_role = "td_role1";
        String td_ssn = "td_ssn1";

        User user = new User(td_id,td_userName, td_firstName, td_lastName, td_email, td_role, td_ssn);

        given(userService.getUserByUsername(td_userName)).willReturn(user);

        mockMvc.perform(get("/users/byusername/" + td_userName)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(td_id))
                .andExpect(jsonPath("$.username").value(td_userName))
                .andExpect(jsonPath("$.firstname").value(td_firstName))
                .andExpect(jsonPath("$.lastname").value(td_lastName))
                .andExpect(jsonPath("$.email").value(td_email))
                .andExpect(jsonPath("$.role").value(td_role))
                .andExpect(jsonPath("$.ssn").value(td_ssn));
    }

    @Test
    public void User_Controller_tc0007_getUserByUsername_Exception() throws Exception {
        String td_userName = "td_userName2";
        String td_message = "Username: td_userName2 not found in User Repository";
        String td_errorDetails = "uri=/users/byusername/td_userName2";

        mockMvc.perform(get("/users/byusername/" + td_userName)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())

                .andExpect(jsonPath("$.message").value(td_message))
                .andExpect(jsonPath("$.errordetails").value(td_errorDetails));
    }
}