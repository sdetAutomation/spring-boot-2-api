package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.exceptions.UserExistsException;
import com.sdet.auto.springboot2api.exceptions.UserNotFoundException;
import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.UserRepository;
import com.sdet.auto.springboot2api.services.UserService;
import com.sdet.auto.springboot2api.services.UserServiceImpl;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplIntegrationTest {
    // to check the Service class, we just need an instance of the Service class created and available as a bean so we
    // can @Autowire it with our test case. This can be achieved using the @TestConfiguration annotation (below)
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    // this creates a Mock for the UserRepository which can be used to bypass the call to the actual UserRepository
    @MockBean
    private UserRepository userRepository;

    @Before
    public void setUp() {
        int  first_user = 0;
        Long td_id1 = 111L;
        String td_userName1 = "td_userName1";
        String td_firstName1 = "td_firstName1";
        String td_lastName1 = "td_lastName1";
        String td_email1 = "td_email1";
        String td_role1 = "td_role1";
        String td_ssn1 = "td_ssn1";
        Long td_id2 = 222L;
        String td_userName2 = "td_userName2";
        String td_firstName2 = "td_firstName2";
        String td_lastName2 = "td_lastName2";
        String td_email2 = "td_email2";
        String td_role2 = "td_role2";
        String td_ssn2 = "td_ssn2";
        Long td_id3 = 333L;
        String td_userName3 = "td_userName3";
        String td_firstName3 = "td_firstName3";
        String td_lastName3 = "td_lastName3";
        String td_email3 = "td_email3";
        String td_role3 = "td_role3";
        String td_ssn3 = "td_ssn3";

        User user1 = new User(td_id1,td_userName1, td_firstName1, td_lastName1, td_email1, td_role1, td_ssn1);
        User user2 = new User(td_id2,td_userName2, td_firstName2, td_lastName2, td_email2, td_role2, td_ssn2);
        User user3 = new User(td_id3,td_userName3, td_firstName3, td_lastName3, td_email3, td_role3, td_ssn3);

        List<User> td_users = Arrays.asList(user1, user2);

        // list out mocks scenarios below.
        Mockito.when(userRepository.findAll()).thenReturn(td_users);
        Mockito.when(userRepository.findById(td_id1)).thenReturn(Optional.ofNullable(td_users.get(first_user)));
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user3);
        Mockito.when(userRepository.findByUsername(td_userName1)).thenReturn(user1);
    }

    @Test
    public void User_Service_tc0001_getAllUsers() {
        int td_array_size = 2;
        int user1 = 0;
        Long td_id1 = 111L;
        String td_userName1 = "td_userName1";
        String td_firstName1 = "td_firstName1";
        String td_lastName1 = "td_lastName1";
        String td_email1 = "td_email1";
        String td_role1 = "td_role1";
        String td_ssn1 = "td_ssn1";

        List<User> users = userService.getAllUsers();

        assertEquals(users.size(), td_array_size);
        assertEquals(users.get(user1).getId(), td_id1);
        assertEquals(users.get(user1).getUsername(), td_userName1);
        assertEquals(users.get(user1).getFirstname(), td_firstName1);
        assertEquals(users.get(user1).getLastname(), td_lastName1);
        assertEquals(users.get(user1).getEmail(), td_email1);
        assertEquals(users.get(user1).getRole(), td_role1);
        assertEquals(users.get(user1).getSsn(), td_ssn1);
    }

    @Test
    public void User_Service_tc0002_getUserById() throws UserNotFoundException {
        Long td_id1 = 111L;
        String td_userName1 = "td_userName1";
        String td_firstName1 = "td_firstName1";
        String td_lastName1 = "td_lastName1";
        String td_email1 = "td_email1";
        String td_role1 = "td_role1";
        String td_ssn1 = "td_ssn1";

        Optional<User> users = userService.getUserById(td_id1);

        assertEquals(users.get().getId(), td_id1);
        assertEquals(users.get().getUsername(), td_userName1);
        assertEquals(users.get().getFirstname(), td_firstName1);
        assertEquals(users.get().getLastname(), td_lastName1);
        assertEquals(users.get().getEmail(), td_email1);
        assertEquals(users.get().getRole(), td_role1);
        assertEquals(users.get().getSsn(), td_ssn1);
    }

    @Test
    public void User_Service_tc0003_getUserById_Exception() {
        Long td_id1 = 999L;
        String td_error_message = "User not found in User Repository";

        try {
            userService.getUserById(td_id1);
        } catch (UserNotFoundException ex) {
            assertEquals(td_error_message, ex.getMessage());
        }
    }

    @Test
    public void User_Service_tc0004_getUserByUsername() {
        Long td_id1 = 111L;
        String td_userName1 = "td_userName1";
        String td_firstName1 = "td_firstName1";
        String td_lastName1 = "td_lastName1";
        String td_email1 = "td_email1";
        String td_role1 = "td_role1";
        String td_ssn1 = "td_ssn1";

        User users = userService.getUserByUsername(td_userName1);

        assertEquals(users.getId(), td_id1);
        assertEquals(users.getUsername(), td_userName1);
        assertEquals(users.getFirstname(), td_firstName1);
        assertEquals(users.getLastname(), td_lastName1);
        assertEquals(users.getEmail(), td_email1);
        assertEquals(users.getRole(), td_role1);
        assertEquals(users.getSsn(), td_ssn1);
    }

    @Test
    public void User_Service_tc0005_createUser() throws UserExistsException {
        Long td_id3 = 333L;
        String td_userName3 = "td_userName3";
        String td_firstName3 = "td_firstName3";
        String td_lastName3 = "td_lastName3";
        String td_email3 = "td_email3";
        String td_role3 = "td_role3";
        String td_ssn3 = "td_ssn3";

        User user = new User(td_id3, td_userName3, td_firstName3, td_lastName3, td_email3, td_role3, td_ssn3);

        User returnUser = userService.createUser(user);

        assertEquals(returnUser.getId(), td_id3);
        assertEquals(returnUser.getUsername(), td_userName3);
        assertEquals(returnUser.getFirstname(), td_firstName3);
        assertEquals(returnUser.getLastname(), td_lastName3);
        assertEquals(returnUser.getEmail(), td_email3);
        assertEquals(returnUser.getRole(), td_role3);
        assertEquals(returnUser.getSsn(), td_ssn3);
    }

    @Test
    public void User_Service_tc0006_createUser_Exception() {
        String td_userName3 = "td_userName1";
        String td_error_message = "User already exists in User Repository";

        User user = new User(null, td_userName3, "", "", "", "", "");

        try {
            userService.createUser(user);
        } catch (UserExistsException ex){
            assertEquals(td_error_message, ex.getMessage());
        }
    }

    @Test
    public void User_Service_tc0007_updateUser() throws UserNotFoundException {
        Long td_id1 = 111L;
        Long td_id3 = 333L;
        String td_userName3 = "td_userName3";
        String td_firstName3 = "td_firstName3";
        String td_lastName3 = "td_lastName3";
        String td_email3 = "td_email3";
        String td_role3 = "td_role3";
        String td_ssn3 = "td_ssn3";

        User user = new User(td_id3, td_userName3, td_firstName3, td_lastName3, td_email3, td_role3, td_ssn3);

        User returnUser = userService.updateUserById(td_id1, user);

        assertEquals(returnUser.getId(), td_id3);
        assertEquals(returnUser.getUsername(), td_userName3);
        assertEquals(returnUser.getFirstname(), td_firstName3);
        assertEquals(returnUser.getLastname(), td_lastName3);
        assertEquals(returnUser.getEmail(), td_email3);
        assertEquals(returnUser.getRole(), td_role3);
        assertEquals(returnUser.getSsn(), td_ssn3);
    }

    @Test
    public void User_Service_tc0008_updateUser_Exception() throws UserNotFoundException {
        Long td_id1 = 999L;
        String td_error_message = "User not found in User Repository, please provide correct user id";

        User user = new User(null, "", "", "", "", "", "");

        try {
            userService.updateUserById(td_id1, user);
        } catch (UserNotFoundException ex){
            assertEquals(td_error_message, ex.getMessage());
        }
    }

    @Test
    public void User_Service_tc0009_deleteUser() {
        Long td_userId = 111L;
        userService.deleteUserById(td_userId);

        Mockito.verify(userRepository, times(1)).deleteById(td_userId);
    }

    @Test
    public void User_Service_tc0010_deleteUser_Exception() {
        Long td_userId = 999L;
        String td_error_message = "400 BAD_REQUEST \"User not found in User Repository, please provide correct user id\"";

        try {
            userService.deleteUserById(td_userId);
        } catch (ResponseStatusException ex) {
            assertEquals(td_error_message, ex.getMessage());
        }
    }
}
