package com.sdet.auto.springboot2api;

import com.sdet.auto.springboot2api.model.User;
import com.sdet.auto.springboot2api.repository.UserRepository;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Before
    public void setUp() {
        User user1 = new User(null,"username1", "firstname1", "lastname1",
                "email1", "role1", "ssn1", null);
        User user2 = new User(null,"username2", "firstname2", "lastname2",
                "email2", "role2", "ssn2", null);
        // perform save
        this.userRepository.save(user1);
        this.userRepository.save(user2);
    }

    @Test
    public void User_Repository_TC0001_testFetchData() {
        User saved_user1 = userRepository.findByUsername("username1");
        User saved_user2 = userRepository.findByUsername("username2");
        // assert user1
//        assertEquals("1", saved_user1.getId().toString());
        assertEquals("username1", saved_user1.getUsername());
        assertEquals("firstname1", saved_user1.getFirstname());
        assertEquals("lastname1", saved_user1.getLastname());
        assertEquals("email1", saved_user1.getEmail());
        assertEquals("role1", saved_user1.getRole());
        assertEquals("ssn1", saved_user1.getSsn());
        // assert user2
//        assertEquals("2", saved_user2.getId().toString());
        assertEquals("username2", saved_user2.getUsername());
        assertEquals("firstname2", saved_user2.getFirstname());
        assertEquals("lastname2", saved_user2.getLastname());
        assertEquals("email2", saved_user2.getEmail());
        assertEquals("role2", saved_user2.getRole());
        assertEquals("ssn2", saved_user2.getSsn());
    }

    @Test
    public void User_Repository_TC0002_testUpdateData() {
        String td_original_record = "username1";
        String td_userName = "td_username";
        String td_firstName = "td_firstName";
        String td_lastName = "td_lastName";
        String td_email = "td_email";
        String td_role = "td_role";
        String td_ssn = "td_ssn";
        // get record
        User saved_user1 = userRepository.findByUsername(td_original_record);
        // set id for later get and assert of record
        Long td_id = saved_user1.getUserId();

        // edit saved user
        saved_user1.setUsername(td_userName);
        saved_user1.setFirstname(td_firstName);
        saved_user1.setLastname(td_lastName);
        saved_user1.setEmail(td_email);
        saved_user1.setRole(td_role);
        saved_user1.setSsn(td_ssn);
        // save user
        userRepository.save(saved_user1);
        // get updated user
        Optional<User> updated_user = userRepository.findById(td_id);

        assertEquals(td_id, updated_user.get().getUserId());
        assertEquals(td_userName, updated_user.get().getUsername());
        assertEquals(td_firstName, updated_user.get().getFirstname());
        assertEquals(td_lastName, updated_user.get().getLastname());
        assertEquals(td_email, updated_user.get().getEmail());
        assertEquals(td_role, updated_user.get().getRole());
        assertEquals(td_ssn, updated_user.get().getSsn());
    }

    @Test
    public void User_Repository_TC0003_deleteTestData() {
        userRepository.deleteAll();

        Optional<User> user = userRepository.findById(1L);

        assertEquals(user.orElse(null), null);
    }
}
