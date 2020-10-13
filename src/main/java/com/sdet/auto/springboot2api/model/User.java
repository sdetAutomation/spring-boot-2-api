package com.sdet.auto.springboot2api.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.hateoas.ResourceSupport;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

// Entity refers to the name of the class.  Represents a table that is stored in a database.
// Defaults to name of class, however, can also declare a different name @Entity(name = "YourName")
@ApiModel("This model is used to create a user")
@Entity
@Table(name = "user") // this will be the name of the table.  Defaults to the entity name if name field not defined
//@JsonIgnoreProperties({"firstname", "lastname"}) -- this is part of static filtering @JsonIgnore (please see that commit)
//@JsonFilter("userFilter") -- this is for MappingJacksonValue filtering
public class User extends ResourceSupport {

    @ApiModelProperty(notes = "Auto generated unique id", required = true, position = 1)
    @Id // this annotation will make variable as a primary key
    @GeneratedValue
    @JsonView(Views.External.class)
    private Long userId; // refactored this name due to hateoas has default field as "id"

    // defaults to field name if you do not define name)
    @ApiModelProperty(notes = "username should contain more than 1 character", example = "sdet.auto", required = false, position = 2)
    @Size(min=2, max=50)
    @NotEmpty(message="Username is a required field.  Please provide a username")
    @Column(name = "USER_NAME", length = 50, nullable = false, unique = true)
    @JsonView(Views.External.class)
    private String username;

    @Size(min=2, max=50, message="FirstName should contain at least 2 characters")
    @Column(name = "FIRST_NAME", length = 50, nullable = false)
    @JsonView(Views.External.class)
    private String firstname;

    @Size(min=2, max=50)
    @Column(name = "LAST_NAME", length = 50, nullable = false)
    @JsonView(Views.External.class)
    private String lastname;

    @Column(name = "EMAIL_ADDRESS", length = 50, nullable = false)
    @JsonView(Views.External.class)
    private String email;

    @Column(name = "ROLE", length = 50, nullable = false)
    @JsonView(Views.Internal.class)
    private String role;

//    @JsonIgnore -- this is part of static filtering @JsonIgnore (please see that commit)
    @Column(name = "SSN", length = 11, nullable = false, unique = true)
    @JsonView(Views.Internal.class)
    private String ssn;

    // order is the owner of the relationship. We don't want to create a foreign key in both tables, we can make user
    // as the foreign key, which will create a column for user in the order table.  User side is the referencing side
    @OneToMany(mappedBy = "user")
    @JsonView(Views.Internal.class)
    private List<Order> orders;

    @Column(name = "ADDRESS")
    private String address;

    // No Argument Constructor
    public User() {
    }

    // Fields Constructor


    public User(Long userId, @NotEmpty(message = "Username is a required field.  Please provide a username")
            String username, @Size(min = 2, message = "FirstName should contain at least 2 characters")
            String firstname, String lastname, String email, String role, String address, String ssn,
                List<Order> orders) {
        this.userId = userId;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
                ", address='" + address + '\'' +
                ", ssn='" + ssn + '\'' +
                ", orders=" + orders +
                '}';
    }
}
