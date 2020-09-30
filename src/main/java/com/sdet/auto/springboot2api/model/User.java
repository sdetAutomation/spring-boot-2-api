package com.sdet.auto.springboot2api.model;

import org.springframework.hateoas.ResourceSupport;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

// Entity refers to the name of the class.  Represents a table that is stored in a database.
// Defaults to name of class, however, can also declare a different name @Entity(name = "YourName")
@Entity
@Table(name = "user") // this will be the name of the table.  Defaults to the entity name if name field not defined
public class User extends ResourceSupport {

    @Id // this annotation will make variable as a primary key
    @GeneratedValue
    private Long userId; // refactored this name due to hateoas has default field as "id"

    // defaults to field name if you do not define name)
    @NotEmpty(message="Username is a required field.  Please provide a username")
    @Column(name = "USER_NAME", length = 50, nullable = false, unique = true)
    private String username;

    @Size(min=2, message="FirstName should contain at least 2 characters")
    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    private String firstname;

    @Column(name = "LAST_NAME", length = 50, nullable = false)
    private String lastname;

    @Column(name = "EMAIL_ADDRESS", length = 50, nullable = false)
    private String email;

    @Column(name = "ROLE", length = 50, nullable = false)
    private String role;

    @Column(name = "SSN", length = 11, nullable = false, unique = true)
    private String ssn;

    // order is the owner of the relationship. We don't want to create a foreign key in both tables, we can make user
    // as the foreign key, which will create a column for user in the order table.  User side is the referencing side
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    // No Argument Constructor
    public User() {
    }

    // Fields Constructor
    public User(Long userId, @NotEmpty(message = "Username is a required field.  Please provide a username")
            String username, @Size(min = 2, message = "FirstName should contain at least 2 characters")
            String firstname, String lastname, String email, String role, String ssn, List<Order> orders) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.ssn = ssn;
        this.orders = orders;
    }


    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    // for troubleshooting
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", ssn='" + ssn + '\'' +
                ", orders=" + orders +
                '}';
    }
}
